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
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * RestTemplate snake 자동 변환을 위한 설정
 *  - Connection pool 생성
 */
@Configuration
public class RestTemplateConfig {

	// snake_case RestTemplate
	@Bean("snakeRestTemplate")
	public RestTemplate snakeRestTemplate() {
		return createRestTemplate(PropertyNamingStrategies.SNAKE_CASE);
	}

	// camelCase RestTemplate
	@Bean("camelRestTemplate")
	public RestTemplate camelRestTemplate() {
		return createRestTemplate(PropertyNamingStrategies.LOWER_CAMEL_CASE);
	}

	private RestTemplate createRestTemplate(PropertyNamingStrategy namingStrategy) {

		//RestTemplate 커넥션풀 설정
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(200);
		connectionManager.setDefaultMaxPerRoute(50);

		CloseableHttpClient httpClient = HttpClients.custom()
			.setConnectionManager(connectionManager)
			.build();

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(namingStrategy);

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper);

		RestTemplate restTemplate = new RestTemplate(factory);
		List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
		converters.removeIf(c -> c instanceof MappingJackson2HttpMessageConverter);
		converters.add(converter);

		return restTemplate;
	}
}
