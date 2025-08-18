package woojooin.planit.global.util.fcm;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import com.google.firebase.messaging.WebpushNotification;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FcmUtil {
	private final FirebaseMessaging messaging;

	/**
	 * 특정 유저(토큰)에 웹 푸시 단건 전송
	 */
	public String sendSingleWebPush(String token, String title, String body, String link,
		Map<String, String> data) throws Exception {
		WebpushNotification notification = WebpushNotification.builder()
			.setTitle(title)
			.setBody(body)
			.setIcon("/icons/icon-192.png")
			.build();

		WebpushConfig webpush = WebpushConfig.builder()
			.setNotification(notification)
			.setFcmOptions(WebpushFcmOptions.withLink(link))
			.putHeader("TTL", "86400")
			.build();

		Message.Builder messageBuilder = Message.builder()
			.setToken(token)
			.setWebpushConfig(webpush);

		if (data != null && !data.isEmpty()) {
			data.forEach(messageBuilder::putData);
		}

		return messaging.send(messageBuilder.build());
	}

}
