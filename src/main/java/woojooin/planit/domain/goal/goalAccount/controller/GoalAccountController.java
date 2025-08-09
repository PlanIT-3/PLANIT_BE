package woojooin.planit.domain.goal.goalAccount.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import woojooin.planit.global.security.CustomUserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import woojooin.planit.domain.goal.goalAccount.dto.res.GoalAccountRes;
import woojooin.planit.domain.goal.goalAccount.service.GoalAccountService;

@RestController
@RequestMapping("/auth/api/goals")
@RequiredArgsConstructor
@Api(value = "목표 할당 계좌 API" ,description = "목표에 할당된 계좌 관련 API로 목표 관리 페이지 내 사용")
@Slf4j
public class GoalAccountController {
    @Autowired
    private GoalAccountService goalAccountService;

    @GetMapping("/{goalId}/accounts")
    public ResponseEntity<GoalAccountRes> getGoalAccounts(@PathVariable Long goalId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getId();
        log.info("[GoalAccountController.getGoalAccountsByMemberId()] - memberId={}, goalId={}", memberId, goalId);

        // 목표 계좌 조회
        GoalAccountRes response = goalAccountService.getGoalAccountsByMemberId(memberId, goalId);
        return ResponseEntity.ok(response);
    }

}
