package woojooin.planit.global.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import woojooin.planit.global.util.codef.dto.res.CodefTokenRes;

@Configuration
public class CacheConfig {

	@Bean
	public Cache<String, Object> localCache() {
		return Caffeine.newBuilder()
			.maximumSize(10_000)
			.expireAfterWrite(10, TimeUnit.MINUTES)
			.build();
	}

	@Bean
	public Cache<String, CodefTokenRes> localCodefCache() {
		return Caffeine.newBuilder()
			.maximumSize(10_000)
			.expireAfterWrite(10, TimeUnit.MINUTES)
			.build();
	}
}
