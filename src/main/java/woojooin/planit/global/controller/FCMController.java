package woojooin.planit.global.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import woojooin.planit.global.dto.FCMTokenDto;
import woojooin.planit.global.service.FCMTokenService;
import woojooin.planit.global.response.Response;

@RestController
@RequestMapping("/auth/api/fcm")
@Api(value = "FCM API", description = "Firebase Cloud Messaging 관련 API")
@RequiredArgsConstructor
@Slf4j
public class FCMController {

    private final FCMTokenService fcmTokenService;

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

    @PostMapping("/test/{memberId}")
    @ApiOperation(value = "FCM 테스트 전송 API", notes = "테스트용 FCM 알림을 전송합니다.")
    public ResponseEntity<Response<String>> sendTestNotification(@PathVariable Long memberId) {
        log.info("FCM 테스트 알림 전송: memberId={}", memberId);
        
        String result = fcmTokenService.sendTestNotification(memberId);
        
        return ResponseEntity.ok(Response.ok(result));
    }
}