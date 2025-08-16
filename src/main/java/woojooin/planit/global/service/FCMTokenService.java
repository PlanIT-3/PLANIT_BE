package woojooin.planit.global.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import woojooin.planit.global.dto.FCMTokenDto;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMTokenService {

    private final FCMService fcmService;
    
    // 메모리 기반 토큰 저장소 (실제로는 DB나 Redis 사용 권장)
    private final Map<Long, String> memberTokens = new ConcurrentHashMap<>();
    private final Map<String, Long> tokenMembers = new ConcurrentHashMap<>();

    /**
     * FCM 토큰 등록
     * @param fcmTokenDto FCM 토큰 정보
     */
    public void registerToken(FCMTokenDto fcmTokenDto) {
        Long memberId = fcmTokenDto.getMemberId();
        String token = fcmTokenDto.getToken();

        // 기존 토큰이 있다면 제거
        String oldToken = memberTokens.get(memberId);
        if (oldToken != null) {
            tokenMembers.remove(oldToken);
        }

        // 새 토큰 등록
        memberTokens.put(memberId, token);
        tokenMembers.put(token, memberId);

        log.info("FCM 토큰 등록 완료: memberId={}, token={}", memberId, token);
    }

    /**
     * FCM 토큰 해제
     * @param token FCM 토큰
     */
    public void unregisterToken(String token) {
        Long memberId = tokenMembers.get(token);
        if (memberId != null) {
            memberTokens.remove(memberId);
            tokenMembers.remove(token);
            log.info("FCM 토큰 해제 완료: memberId={}, token={}", memberId, token);
        }
    }

    /**
     * 회원의 FCM 토큰 조회
     * @param memberId 회원 ID
     * @return FCM 토큰
     */
    public String getTokenByMemberId(Long memberId) {
        return memberTokens.get(memberId);
    }

    /**
     * 회원에게 알림 전송
     * @param memberId 회원 ID
     * @param title 알림 제목
     * @param body 알림 내용
     * @return 전송 결과
     */
    public String sendNotificationToMember(Long memberId, String title, String body) {
        String token = getTokenByMemberId(memberId);
        if (token != null) {
            return fcmService.sendNotification(token, title, body);
        } else {
            log.warn("FCM 토큰이 없습니다: memberId={}", memberId);
            return null;
        }
    }

    /**
     * 테스트 알림 전송
     * @param memberId 회원 ID
     * @return 전송 결과
     */
    public String sendTestNotification(Long memberId) {
        String title = "🔔 PlanIT 테스트 알림";
        String body = "FCM 설정이 정상적으로 완료되었습니다!";
        return sendNotificationToMember(memberId, title, body);
    }

    /**
     * 목표 달성 알림 전송
     * @param memberId 회원 ID
     * @param goalName 목표 이름
     * @param achievementRate 달성률
     */
    public void sendGoalAchievementNotification(Long memberId, String goalName, int achievementRate) {
        String token = getTokenByMemberId(memberId);
        if (token != null) {
            fcmService.sendGoalAchievementNotification(token, goalName, achievementRate);
        }
    }

    /**
     * 리밸런싱 알림 전송
     * @param memberId 회원 ID
     * @param goalName 목표 이름
     */
    public void sendRebalanceNotification(Long memberId, String goalName) {
        String token = getTokenByMemberId(memberId);
        if (token != null) {
            fcmService.sendRebalanceNotification(token, goalName);
        }
    }

    /**
     * 예적금 만기 알림 전송
     * @param memberId 회원 ID
     * @param accountName 계좌명
     * @param daysUntilMaturity 만기까지 남은 일수
     */
    public void sendDepositMaturityNotification(Long memberId, String accountName, int daysUntilMaturity) {
        String token = getTokenByMemberId(memberId);
        if (token != null) {
            fcmService.sendDepositMaturityNotification(token, accountName, daysUntilMaturity);
        }
    }
}