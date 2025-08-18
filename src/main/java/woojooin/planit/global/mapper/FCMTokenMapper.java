package woojooin.planit.global.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FCMTokenMapper {

	/**
	 * FCM 토큰 저장/업데이트
	 * @param memberId 회원 ID
	 * @param fcmToken FCM 토큰
	 */
	void updateFcmToken(@Param("memberId") Long memberId, @Param("fcmToken") String fcmToken);

	/**
	 * 회원의 FCM 토큰 조회
	 * @param memberId 회원 ID
	 * @return FCM 토큰
	 */
	String findFcmTokenById(@Param("memberId") Long memberId);

	/**
	 * FCM 토큰 삭제 (NULL로 설정)
	 * @param memberId 회원 ID
	 */
	void deleteFcmTokenById(@Param("memberId") Long memberId);
}