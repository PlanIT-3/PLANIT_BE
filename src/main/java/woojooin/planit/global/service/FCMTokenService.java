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

    // FCMService는 알림 발송용이므로 일단 제외
    
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

    // ===== 알림 발송 관련 메서드들은 RabbitMQ로 분리 예정 =====
    
    /*
    // FCMService 의존성이 필요한 메서드들 - 추후 RabbitMQ Consumer에서 구현 예정
    
    public String sendNotificationToMember(Long memberId, String title, String body) {
        String token = getTokenByMemberId(memberId);
        if (token != null) {
            return fcmService.sendNotification(token, title, body);
        } else {
            log.warn("FCM 토큰이 없습니다: memberId={}", memberId);
            return null;
        }
    }

    public String sendTestNotification(Long memberId) {
        String title = "🔔 PlanIT 테스트 알림";
        String body = "FCM 설정이 정상적으로 완료되었습니다!";
        return sendNotificationToMember(memberId, title, body);
    }

    public void sendGoalAchievementNotification(Long memberId, String goalName, int achievementRate) {
        String token = getTokenByMemberId(memberId);
        if (token != null) {
            fcmService.sendGoalAchievementNotification(token, goalName, achievementRate);
        }
    }

    public void sendRebalanceNotification(Long memberId, String goalName) {
        String token = getTokenByMemberId(memberId);
        if (token != null) {
            fcmService.sendRebalanceNotification(token, goalName);
        }
    }

    public void sendDepositMaturityNotification(Long memberId, String accountName, int daysUntilMaturity) {
        String token = getTokenByMemberId(memberId);
        if (token != null) {
            fcmService.sendDepositMaturityNotification(token, accountName, daysUntilMaturity);
        }
    }
    */
}