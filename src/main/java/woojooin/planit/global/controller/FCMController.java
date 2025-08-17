package woojooin.planit.global.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.global.dto.FCMTokenDto;
import woojooin.planit.global.response.Response;
import woojooin.planit.global.service.FCMTokenService;

@RestController
@RequestMapping("/auth/api/fcm")
@Api(value = "FCM API", description = "Firebase Cloud Messaging 관련 API")
@RequiredArgsConstructor
@Slf4j
public class FCMController {

	private final FCMTokenService fcmTokenService;

	@Value("${firebase.vapid.key}")
	private String vapidKey;

	@PostMapping("/register")
	@ApiOperation(value = "FCM 토큰 등록 API", notes = "디바이스의 FCM 토큰을 서버에 등록합니다.")
	public ResponseEntity<Response<Void>> registerToken(@RequestBody FCMTokenDto fcmTokenDto) {
		log.info("FCM 토큰 등록 요청: memberId={}, token={}", fcmTokenDto.getMemberId(), fcmTokenDto.getToken());

		fcmTokenService.registerToken(fcmTokenDto);

		return ResponseEntity.ok(Response.ok());
	}

	@DeleteMapping("/unregister")
	@ApiOperation(value = "FCM 토큰 해제 API", notes = "등록된 FCM 토큰을 해제합니다.")
	public ResponseEntity<Response<Void>> unregisterToken(@RequestBody FCMTokenDto fcmTokenDto) {
		log.info("FCM 토큰 해제 요청: token={}", fcmTokenDto.getToken());

		fcmTokenService.unregisterToken(fcmTokenDto.getToken());

		return ResponseEntity.ok(Response.ok());
	}

	@GetMapping("/vapid-key")
	@ApiOperation(value = "VAPID 공개키 조회 API", notes = "클라이언트에서 FCM 토큰 생성 시 사용할 VAPID 키를 반환합니다.")
	public ResponseEntity<Response<String>> getVapidKey() {
		log.info("VAPID 키 요청");
		return ResponseEntity.ok(Response.ok(vapidKey));
	}

    /*
    // 테스트 API는 FCMService 의존성이 필요하므로 RabbitMQ로 분리 예정
    @PostMapping("/test/{memberId}")
    @ApiOperation(value = "FCM 테스트 전송 API", notes = "테스트용 FCM 알림을 전송합니다.")
    public ResponseEntity<Response<String>> sendTestNotification(@PathVariable Long memberId) {
        log.info("FCM 테스트 알림 전송: memberId={}", memberId);

        String result = fcmTokenService.sendTestNotification(memberId);

        return ResponseEntity.ok(Response.ok(result));
    }
    */
}