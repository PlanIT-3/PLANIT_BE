package woojooin.planit.global.util.codef;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.account.domain.Account;
import woojooin.planit.domain.account.mapper.AccountMapper;
import woojooin.planit.global.exception.BusinessException;
import woojooin.planit.global.response.ResponseCode;
import woojooin.planit.global.util.ConnectionUtil;
import woojooin.planit.global.util.codef.dto.CodefResponse;
import woojooin.planit.global.util.codef.dto.account.CodefAccountData;
import woojooin.planit.global.util.codef.dto.account.CodefAccountResponse;
import woojooin.planit.global.util.codef.dto.account.CodefSecuritiesAccountData;
import woojooin.planit.global.util.codef.dto.account.ResDepositTrust;
import woojooin.planit.global.util.codef.dto.connectedId.AccountDto;
import woojooin.planit.global.util.codef.dto.connectedId.add.ConnectedIdAddReq;
import woojooin.planit.global.util.codef.dto.connectedId.create.ConnectedIdCreateReq;
import woojooin.planit.global.util.codef.dto.connectedId.create.ConntectedIdCreateRes;
import woojooin.planit.global.util.codef.dto.token.CodefTokenRes;

@Slf4j
@Component
@RequiredArgsConstructor
public class CodefAccountUtil {

	private final Cache<String, CodefTokenRes> localCache;
	private final AccountMapper accountMapper;

	@Qualifier("snakeRestTemplate")
	private final RestTemplate snakeRestTemplate;

	@Qualifier("camelRestTemplate")
	private final RestTemplate camelRestTemplate;

	@Value("${codef.base-url}")
	private String CODEF_URL;

	@Value("${codef.api-url}")
	private String CODEF_API_URL;

	@Value("${codef.public.key}")
	private String CODEF_PUBLIC_KEY;

	@Value("${codef.client.id}")
	private String CODEF_CLIENT_ID;

	@Value("${codef.client.secret}")
	private String CODEF_CLIENT_SECRET;

	private final static String BASIC_PREFIX = "Basic ";
	private final static String BEARER_PREFIX = "Bearer ";
	private final static String AUTHORIZATION_HEADER = "Authorization";
	private static final String TOKEN_CACHE_KEY = "codef_access_token";

	public CodefAccountData getAccountData(String connectedId, String organization) {

		String url = CODEF_API_URL + "/v1/kr/bank/p/account/account-list";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(AUTHORIZATION_HEADER, BEARER_PREFIX + getAccessToken().accessToken());

		Map<String, Object> body = new HashMap<>();
		body.put("organization", organization);
		body.put("connectedId", connectedId);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

		ResponseEntity<String> responseEntity = camelRestTemplate.exchange(
			url,
			HttpMethod.POST,
			entity,
			String.class
		);

		String resString = responseEntity.getBody();

		TypeReference<CodefAccountResponse> type = new TypeReference<CodefAccountResponse>() {
		};

		CodefAccountResponse response = ConnectionUtil.decodeUrlStringToDto(resString, type,
			ConnectionUtil.CAMEL);

		return response.getData();
	}

	public CodefSecuritiesAccountData getSecuritiesAccountData(String connectedId, String organization) {
		String url = CODEF_API_URL + "/v1/kr/stock/a/account/account-list";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(AUTHORIZATION_HEADER, BEARER_PREFIX + getAccessToken().accessToken());

		Map<String, Object> body = new HashMap<>();
		body.put("organization", organization);
		body.put("connectedId", connectedId);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

		ResponseEntity<String> responseEntity = camelRestTemplate.exchange(
			url,
			HttpMethod.POST,
			entity,
			String.class
		);

		String resString = responseEntity.getBody();

		TypeReference<CodefSecuritiesAccountData> type = new TypeReference<CodefSecuritiesAccountData>() {
		};

		return ConnectionUtil.decodeUrlStringToDto(resString, type, ConnectionUtil.CAMEL);
	}

	/**
	 * codef_path : /oauth/token
	 * codef token 발급 메서드
	 * @return
	 */
	private CodefTokenRes getAccessTokenFromCodef() {

		String url = CODEF_URL + "/oauth/token";
		String authKey = CODEF_CLIENT_ID + ":" + CODEF_CLIENT_SECRET;
		String encodedAuthKey = Base64.getEncoder().encodeToString(authKey.getBytes(StandardCharsets.UTF_8));

		log.info("authKey = {}", encodedAuthKey);

		//header 값 세팅
		HttpHeaders headers = new HttpHeaders();
		headers.set(AUTHORIZATION_HEADER, BASIC_PREFIX + encodedAuthKey);
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		//body 값 세팅
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "client_credentials");
		body.add("scope", "read");

		//HTTP POST 요청 및 응답
		return snakeRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), CodefTokenRes.class)
			.getBody();
	}

	/**
	 * token값 반환 메서드
	 * 토큰이 만료되었으면 재발급
	 */
	public CodefTokenRes getAccessToken() {
		CodefTokenRes cachedToken = localCache.getIfPresent(TOKEN_CACHE_KEY);

		if (cachedToken == null) {
			cachedToken = getAccessTokenFromCodef();
			log.info("cachedToken:{}", cachedToken);
			localCache.put(TOKEN_CACHE_KEY, cachedToken);
		}

		return cachedToken;
	}

	/**
	 * codef_path : /v1/account/create
	 * connected_id를 발급하기 위한 user register 기능
	 * AccountDto - 사용자의 실제 계좌 정보를 담은 객체
	 * @param accountList
	 */
	public ConntectedIdCreateRes registerConnectedId(List<AccountDto> accountList) {
		String url = CODEF_API_URL + "/v1/account/create";

		// Encrypt passwords in accountList
		for (AccountDto account : accountList) {
			if (account.getPassword() != null && !account.getPassword().isEmpty()) {
				String originalPassword = account.getPassword();

				// 패스워드 길이 검증
				byte[] passwordBytes = originalPassword.getBytes(StandardCharsets.UTF_8);
				log.info("Encrypting password for account {}, length: {} bytes",
					account.getId(), passwordBytes.length);

				try {
					// 공개 키 정보 확인
					checkPublicKeyInfo(CODEF_PUBLIC_KEY);

					// RSA 키 크기에 따른 최대 데이터 크기 확인
					byte[] keyBytes = Base64.getDecoder().decode(CODEF_PUBLIC_KEY);
					X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
					KeyFactory keyFactory = KeyFactory.getInstance("RSA");
					RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(spec);

					int keySize = publicKey.getModulus().bitLength();
					int maxDataSize = (keySize / 8) - 11; // PKCS1 패딩 기준

					if (passwordBytes.length > maxDataSize) {
						log.error("Password for account {} is too long: {} bytes, max allowed: {} bytes for {}-bit key",
							account.getId(), passwordBytes.length, maxDataSize, keySize);
						throw new IllegalArgumentException(
							String.format("Password for account %s exceeds maximum size (%d bytes) for %d-bit RSA key",
								account.getId(), maxDataSize, keySize));
					}

					String encryptedPassword = encryptPassword(originalPassword, CODEF_PUBLIC_KEY);
					account.setPassword(encryptedPassword);
					log.info("Password encryption successful for account: {}", account.getId());

				} catch (Exception e) {
					log.error("Password encryption failed for account: {}", account.getId(), e);
					throw new RuntimeException("Password encryption failed for account: " + account.getId(), e);
				}
			}
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(AUTHORIZATION_HEADER, BEARER_PREFIX + getAccessToken().accessToken());

		ConnectedIdCreateReq body = new ConnectedIdCreateReq(accountList);
		HttpEntity<ConnectedIdCreateReq> entity = new HttpEntity<>(body, headers);

		ResponseEntity<String> responseEntity = camelRestTemplate.exchange(
			url,
			HttpMethod.POST,
			entity,
			String.class
		);

		String resString = responseEntity.getBody();
		TypeReference<CodefResponse<ConntectedIdCreateRes>> type = new TypeReference<CodefResponse<ConntectedIdCreateRes>>() {
		};

		CodefResponse<ConntectedIdCreateRes> res = ConnectionUtil.decodeUrlStringToDto(resString, type,
			ConnectionUtil.CAMEL);

		ConntectedIdCreateRes result = res.getData();
		
		// errorList가 존재하는지 확인하고 예외 발생
		if (result.getErrorList() != null && !result.getErrorList().isEmpty()) {
			ConntectedIdCreateRes.SuccessItem firstError = result.getErrorList().get(0);
			log.error("계좌 연동 실패: {}", firstError.getMessage());
			
			// 특정 에러 코드에 따른 구분된 예외 처리
			if ("CF-04004".equals(firstError.getCode())) {
				throw new BusinessException(ResponseCode.ACCOUNT_ALREADY_REGISTERED);
			} else {
				throw new BusinessException(ResponseCode.ACCOUNT_CONNECTION_FAILED);
			}
		}

		return result;
	}

	// 공개 키 정보 확인 메서드 추가
	private void checkPublicKeyInfo(String publicKeyStr) {
		try {
			byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(spec);

			int keySize = publicKey.getModulus().bitLength();
			int maxDataSizePKCS1 = (keySize / 8) - 11; // PKCS1 패딩 기준
			int maxDataSizeOAEP = (keySize / 8) - 42; // OAEP 패딩 기준

			log.info("RSA Public Key Info:");
			log.info("Key size: {} bits", keySize);
			log.info("Max data size (PKCS1): {} bytes", maxDataSizePKCS1);
			log.info("Max data size (OAEP): {} bytes", maxDataSizeOAEP);
			log.info("Public key length: {} bytes (Base64 decoded)", keyBytes.length);

		} catch (Exception e) {
			log.error("Failed to analyze public key", e);
		}
	}

	/**
	 * codef_path : /v1/account/add
	 * 유저의 connected_id를 기반으로 기관 추가 기능
	 * AccountDto - 사용자의 실제 계좌 정보를 담은 객체
	 * @param accountList
	 */
	public ConntectedIdCreateRes addAccount(List<AccountDto> accountList, String connectedId) {
		String url = CODEF_API_URL + "/v1/account/add";

		// Encrypt passwords in accountList
		for (AccountDto account : accountList) {
			if (account.getPassword() != null && !account.getPassword().isEmpty()) {
				try {
					account.setPassword(encryptPassword(account.getPassword(), CODEF_PUBLIC_KEY));
				} catch (Exception e) {
					log.error("Password encryption failed", e);
					throw new RuntimeException("Password encryption failed", e);
				}
			}
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(AUTHORIZATION_HEADER, BEARER_PREFIX + getAccessToken().accessToken());

		ConnectedIdAddReq body = new ConnectedIdAddReq(accountList, connectedId);

		HttpEntity<ConnectedIdAddReq> entity = new HttpEntity<>(body, headers);

		ResponseEntity<String> responseEntity = camelRestTemplate.exchange(
			url,
			HttpMethod.POST,
			entity,
			String.class
		);

		String encodedBody = responseEntity.getBody();
		TypeReference<CodefResponse<ConntectedIdCreateRes>> type = new TypeReference<CodefResponse<ConntectedIdCreateRes>>() {
		};

		CodefResponse<ConntectedIdCreateRes> response = ConnectionUtil.decodeUrlStringToDto(encodedBody, type,
			ConnectionUtil.CAMEL);

		ConntectedIdCreateRes result = response.getData();
		
		// errorList가 존재하는지 확인하고 예외 발생
		if (result.getErrorList() != null && !result.getErrorList().isEmpty()) {
			ConntectedIdCreateRes.SuccessItem firstError = result.getErrorList().get(0);
			log.error("계좌 추가 실패: {}", firstError.getMessage());
			
			// 특정 에러 코드에 따른 구분된 예외 처리
			if ("CF-04004".equals(firstError.getCode())) {
				throw new BusinessException(ResponseCode.ACCOUNT_ALREADY_REGISTERED);
			} else {
				throw new BusinessException(ResponseCode.ACCOUNT_CONNECTION_FAILED);
			}
		}

		return result;
	}

	/**
	 * codef_path : /v1/account/delete
	 * 유저의 connected_id를 기반으로 기관 추가 기능
	 * AccountDto - 사용자의 실제 계좌 정보를 담은 객체
	 * @param accountList
	 */
	public ConntectedIdCreateRes deleteAccount(@Valid List<AccountDto> accountList, String connectedId) {
		String url = CODEF_API_URL + "/v1/account/delete";

		// Encrypt passwords in accountList
		for (AccountDto account : accountList) {
			if (account.getPassword() != null && !account.getPassword().isEmpty()) {
				try {
					account.setPassword(encryptPassword(account.getPassword(), CODEF_PUBLIC_KEY));
				} catch (Exception e) {
					log.error("Password encryption failed", e);
					throw new RuntimeException("Password encryption failed", e);
				}
			}
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(AUTHORIZATION_HEADER, BEARER_PREFIX + getAccessToken().accessToken());

		ConnectedIdAddReq body = new ConnectedIdAddReq(accountList, connectedId);

		HttpEntity<ConnectedIdAddReq> entity = new HttpEntity<>(body, headers);

		ResponseEntity<String> responseEntity = camelRestTemplate.exchange(
			url,
			HttpMethod.POST,
			entity,
			String.class
		);

		String encodedBody = responseEntity.getBody();
		TypeReference<CodefResponse<ConntectedIdCreateRes>> type = new TypeReference<>() {
		};

		CodefResponse<ConntectedIdCreateRes> response = ConnectionUtil.decodeUrlStringToDto(encodedBody, type,
			ConnectionUtil.CAMEL);

		log.info("[CodefAccountUtil.deleteAccount()] - response {}", response.toString());

		return response.getData();
	}

	public String encryptPassword(String plainText, String publicKeyPem) throws Exception {
		// 1. PEM 헤더/푸터 제거 및 줄바꿈 삭제
		String cleanKey = publicKeyPem
			.replaceAll("-----BEGIN PUBLIC KEY-----", "")
			.replaceAll("-----END PUBLIC KEY-----", "")
			.replaceAll("\\s", "");

		// 2. Base64 디코딩 → 바이트 배열
		byte[] keyBytes = Base64.getDecoder().decode(cleanKey);

		// 3. 공개키 객체 생성
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);


		// 4. RSA 암호화
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  // Codef는 PKCS1Padding 사용
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));

		// 5. 암호화된 결과를 Base64 인코딩하여 반환
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

}
