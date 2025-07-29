package woojooin.planit.global.util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class UrlEncodeUtil {

	private static String URL;

	/**
	 * URL 인코딩 문자열을 DTO로 변환하는 메서드
	 *
	 * @param encoded : url 인코딩된 문자열
	 * @param typeReference : 변환할 타입
	 * @param namingStrategy : 문자열의 네이밍 전략
	 * @return : 제네릭 클래스로 직렬화된 DTO 객체
	 * @param <T>
	 */
	public static <T> T decodeToDto(String encoded, TypeReference<T> typeReference,
		PropertyNamingStrategy namingStrategy) {
		try {
			String decodedJson = URLDecoder.decode(encoded, StandardCharsets.UTF_8);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setPropertyNamingStrategy(namingStrategy);
			return objectMapper.readValue(decodedJson, typeReference);
		} catch (Exception e) {
			throw new RuntimeException("Decoding failed", e);
		}
	}
}
