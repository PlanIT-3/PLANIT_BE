package woojooin.planit.domain.goal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import woojooin.planit.domain.goal.api.dto.res.GoalAccountRes;
import woojooin.planit.domain.goal.api.dto.res.GoalDepositAmountRes;
import woojooin.planit.domain.goal.api.dto.res.GoalIsaRes;
import woojooin.planit.domain.goal.mapper.GoalAccountMapper;
import woojooin.planit.global.config.GoalAmountRes;


import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class GoalAccountService {

    @Autowired
    private GoalAccountMapper goalAccountMapper;

    public GoalAccountRes getGoalAccountsByMemberId(Long memberId, Long goalId) {
        log.info("[GoalAccountService.getGoalAccountsByMemberId()] - memberId={}", memberId, goalId);
        // ISA 타입 계좌 조회
        List<GoalIsaRes> isaAccounts = goalAccountMapper.findIsaByMemberId(memberId, goalId);

        log.info("[GoalAccountService.getGoalAccountsByMemberId()] - ISA accounts size={}, memberId ={}, goalId = {}", isaAccounts.size(), memberId, goalId);
        // Deposit 타입 상품 조회
        List<GoalDepositAmountRes> depositProducts = goalAccountMapper.findDepositByMemberId(memberId, goalId);
        log.info("[GoalAccountService.getGoalAccountsByMemberId()] - Deposit products size={}, memberId={}, goalId={}", depositProducts.size(), memberId, goalId);

        return GoalAccountRes.builder()
                .goalIsaList(isaAccounts)
                .goalDepositList(depositProducts)
                .build();
    }

    public GoalAmountRes getGoalAmountsByMemberId(Long memberId, Long goalId) {
        BigDecimal targetAmount = BigDecimal.ZERO;
        GoalAmountRes goalAccountRes = new GoalAmountRes();
        log.info("[GoalAccountService.getGoalAmountsByMemberId()] - memberId={}, goalId={}", memberId, goalId);

        targetAmount= goalAccountMapper.getTargetAmount(memberId, goalId);
        goalAccountRes.setTargetAmount(targetAmount);
        return goalAccountRes;

    }
}
