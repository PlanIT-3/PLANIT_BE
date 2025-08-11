package woojooin.planit.domain.openAi.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import woojooin.planit.domain.openAi.dto.res.GoalProgressRes;
import woojooin.planit.domain.openAi.dto.res.InvestReportRes;
import woojooin.planit.domain.openAi.dto.res.InvestTypeRes;
import woojooin.planit.domain.openAi.service.GoalOpenAiService;
import woojooin.planit.domain.openAi.service.InvestOpenAiService;
import woojooin.planit.domain.openAi.service.InvestTypeOpenAiService;
import woojooin.planit.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/openai")
@Api(value = "오픈 AI API", description = "오픈 AI로 투자 관련 기능을 제공하는 API")
@Slf4j
@RequiredArgsConstructor
public class OpenAiController {
    private final InvestOpenAiService investOpenAiService;
    private final GoalOpenAiService GoalOpenAiService;
    private final InvestTypeOpenAiService InvestTypeOpenAiService;

    @GetMapping("/recommendated-investment")
    public ResponseEntity<InvestReportRes> getInvestmentAdvice(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getId();

        InvestReportRes response = investOpenAiService.getInvestmentAdvice(memberId);

        log.info("[OpenAiController.getInvestmentAdvice()] - 응답 데이터: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{goalId}/goal-progress")
    public ResponseEntity<GoalProgressRes> getGoalProgressAdvice(@PathVariable Long goalId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getId();

        GoalProgressRes response = GoalOpenAiService.getGoalProgressAdvice(memberId, goalId);

        log.info("[OpenAiController.getGoalProgressAdvice()] - 응답 데이터: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/invest-type-advice")
    public ResponseEntity<InvestTypeRes> getInvestTypeAdvice(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getId();
        InvestTypeRes response = InvestTypeOpenAiService.getInvestTypeAdvice(memberId);
        log.info("[OpenAiController.getInvestTypeAdvice()] - 응답 데이터: {}", response);
        return ResponseEntity.ok(response);
    }

}
