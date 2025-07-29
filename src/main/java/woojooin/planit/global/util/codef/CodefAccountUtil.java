package woojooin.planit.global.util.codef;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

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
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.benmanes.caffeine.cache.Cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.global.util.UrlEncodeUtil;
import woojooin.planit.global.util.codef.dto.CodefResponse;
import woojooin.planit.global.util.codef.dto.connectedId.AccountDto;
import woojooin.planit.global.util.codef.dto.connectedId.create.ConnectedIdCreateReq;
import woojooin.planit.global.util.codef.dto.connectedId.create.ConntectedIdCreateRes;
import woojooin.planit.global.util.codef.dto.token.CodefTokenRes;

@Slf4j
@Component
@RequiredArgsConstructor
public class CodefAccountUtil {

	private final Cache<String, CodefTokenRes> localCache;

	@Qualifier("snakeRestTemplate")
	private final RestTemplate snakeRestTemplate;

	@Qualifier("camelRestTemplate")
	private final RestTemplate camelRestTemplate;

	@Value("${codef.base-url}")
	private String CODEF_URL;

	@Value("${codef.api-url}")
	private String CODEF_API_URL;

	@Value("${codef.client.id}")
	private String CODEF_CLIENT_ID;

	@Value("${codef.client.secret}")
	private String CODEF_CLIENT_SECRET;

	private final static String BASIC_PREFIX = "Basic ";
	private final static String BEARER_PREFIX = "Bearer ";
	private final static String AUTHORIZATION_HEADER = "Authorization";
	private static final String TOKEN_CACHE_KEY = "codef_access_token";

	public static PropertyNamingStrategy SNAKE = PropertyNamingStrategy.SNAKE_CASE;
	public static PropertyNamingStrategy CAMEL = PropertyNamingStrategy.LOWER_CAMEL_CASE;

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

		CodefResponse<ConntectedIdCreateRes> res = UrlEncodeUtil.decodeToDto(resString, type, CAMEL);

		return res.getData();
	}

}
