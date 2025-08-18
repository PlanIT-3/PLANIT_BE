package woojooin.planit.global.fcm.service;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMService {

	private final FirebaseMessaging firebaseMessaging;

	/**
	 * 단일 디바이스에 푸시 알림 전송
	 * @param token 디바이스 토큰
	 * @param title 알림 제목
	 * @param body 알림 내용
	 * @return 전송 결과 (메시지 ID)
	 */
	public String sendNotification(String token, String title, String body) {
		try {
			Notification notification = Notification.builder()
				.setTitle(title)
				.setBody(body)
				.build();

			Message message = Message.builder()
				.setToken(token)
				.setNotification(notification)
				.build();

			String response = firebaseMessaging.send(message);
			log.info("FCM 알림 전송 성공: token={}, messageId={}", token, response);
			return response;
		} catch (Exception e) {
			log.error("FCM 알림 전송 실패: token={}, error={}", token, e.getMessage());
			throw new RuntimeException("FCM 알림 전송 실패", e);
		}
	}

	/**
	 * 목표 달성 알림 전송
	 * @param token 디바이스 토큰
	 * @param goalName 목표 이름
	 * @param achievementRate 달성률
	 */
	public void sendGoalAchievementNotification(String token, String goalName, int achievementRate) {
		String title = "🎉 목표 달성 축하합니다!";
		String body = String.format("%s 목표를 %d%% 달성했습니다!", goalName, achievementRate);
		sendNotification(token, title, body);
	}

	/**
	 * 리밸런싱 알림 전송
	 * @param token 디바이스 토큰
	 * @param goalName 목표 이름
	 */
	public void sendRebalanceNotification(String token, String goalName) {
		String title = "📊 리밸런싱 권장";
		String body = String.format("%s 목표의 포트폴리오 리밸런싱을 검토해보세요.", goalName);
		sendNotification(token, title, body);
	}

	/**
	 * 예적금 만기 알림 전송
	 * @param token 디바이스 토큰
	 * @param accountName 계좌명
	 * @param daysUntilMaturity 만기까지 남은 일수
	 */
	public void sendDepositMaturityNotification(String token, String accountName, int daysUntilMaturity) {
		String title = "⏰ 예적금 만기 알림";
		String body = String.format("%s 계좌가 %d일 후 만료됩니다.", accountName, daysUntilMaturity);
		sendNotification(token, title, body);
	}
}