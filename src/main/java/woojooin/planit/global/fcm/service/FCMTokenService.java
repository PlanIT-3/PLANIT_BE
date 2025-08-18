package woojooin.planit.global.fcm.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.global.fcm.dto.FCMTokenDto;
import woojooin.planit.global.fcm.mapper.FCMTokenMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMTokenService {

	private final FCMTokenMapper fcmTokenMapper;

	/**
	 * FCM 토큰 등록
	 * @param fcmTokenDto FCM 토큰 정보
	 */
	public void registerToken(FCMTokenDto fcmTokenDto) {
		Long memberId = fcmTokenDto.getMemberId();
		String token = fcmTokenDto.getToken();

		fcmTokenMapper.updateFcmToken(memberId, token);

		log.info("FCM 토큰 등록 완료: memberId={}, token={}", memberId, token);
	}

	/**
	 * FCM 토큰 해제
	 * @param memberId 회원 ID
	 */
	public void unregisterToken(Long memberId) {
		fcmTokenMapper.deleteFcmTokenById(memberId);
		log.info("FCM 토큰 해제 완료: memberId={}", memberId);
	}

	/**
	 * 회원의 FCM 토큰 조회
	 * @param memberId 회원 ID
	 * @return FCM 토큰
	 */
	public String getTokenByMemberId(Long memberId) {
		return fcmTokenMapper.findFcmTokenById(memberId);
	}
}