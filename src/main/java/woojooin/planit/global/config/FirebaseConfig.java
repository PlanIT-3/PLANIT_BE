package woojooin.planit.global.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FirebaseConfig {

	@Value("${firebase.enabled:false}")
	private boolean firebaseEnabled;

	@Value("classpath:firebase/firebase-admin-sdk.json")
	private Resource serviceAccountJson;

	// Firebase가 활성화되었을 때만 Bean 생성
	@Bean
	public FirebaseApp firebaseApp() throws IOException {
		if (!firebaseEnabled) {
			return null; // Firebase가 비활성화된 경우 null 반환
		}
		
		try (InputStream is = serviceAccountJson.getInputStream()) {
			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(is))
				.build();

			if (FirebaseApp.getApps().isEmpty()) {
				return FirebaseApp.initializeApp(options);
			} else {
				return FirebaseApp.getInstance();
			}
		}
	}

	@Bean
	public FirebaseMessaging firebaseMessaging() throws IOException {
		FirebaseApp app = firebaseApp();
		if (app == null) {
			return null; // Firebase가 비활성화된 경우 null 반환
		}
		return FirebaseMessaging.getInstance(app);
	}
}

