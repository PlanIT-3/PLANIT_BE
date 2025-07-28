package woojooin.planit.global.config;

import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

/**
 * RestTemplate snake 자동 변환을 위한 설정
 *  - Connection pool 생성
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
		// 1. 커넥션 풀 설정
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(200);              // 전체 커넥션 최대값
		connectionManager.setDefaultMaxPerRoute(50);     // 도메인 당 최대 커넥션 수

		CloseableHttpClient httpClient = HttpClients.custom()
			.setConnectionManager(connectionManager)
			.build();

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

		// 2. ObjectMapper에 snake_case 설정
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

		MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
		jacksonConverter.setObjectMapper(objectMapper);

		// 3. RestTemplate 생성 및 커스텀 컨버터 등록
		RestTemplate restTemplate = new RestTemplate(factory);
		List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
		converters.removeIf(c -> c instanceof MappingJackson2HttpMessageConverter); // 기존 제거
		converters.add(jacksonConverter);

		return restTemplate;
	}
}
