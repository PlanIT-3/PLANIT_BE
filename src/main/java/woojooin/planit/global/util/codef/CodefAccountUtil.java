package woojooin.planit.global.util.codef;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import woojooin.planit.global.util.codef.dto.res.CodefTokenRes;

@Slf4j
@Component
public class CodefAccountUtil {

	@Value("${codef.base-url}")
	private String CODEF_URL;

	@Value("${codef.client.id}")
	private String CODEF_CLIENT_ID;

	@Value("${codef.client.secret}")
	private String CODEF_CLIENT_SECRET;

	private final static String BASIC_PREFIX = "Basic ";
	private final static String BEARER_PREFIX = "Bearer ";
	private final static String AUTHORIZATION_HEADER = "Authorization";

	/**
	 * codef 사용자 응답 및 요청 데이터
	 * @return
	 */
	public CodefTokenRes getAccessToken() {
		RestTemplate restTemplate = new RestTemplate();

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
		return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), CodefTokenRes.class)
			.getBody();
	}

	public static void registerConnectedId() {
		RestTemplate restTemplate = new RestTemplate();

	}
}
