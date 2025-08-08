package woojooin.planit.domain.goal.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import woojooin.planit.domain.goal.domain.Bank;
import woojooin.planit.domain.goal.domain.Goal;
import woojooin.planit.domain.goal.dto.GoalAccountRateResponse;
import woojooin.planit.domain.goal.mapper.GoalMapper;
import woojooin.planit.domain.goal.isa.dto.res.IsaAccountProductRes;
import woojooin.planit.domain.goal.isa.service.IsaAccountService;
import woojooin.planit.global.exception.BusinessException;
import woojooin.planit.global.response.ResponseCode;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalSettingService {
    private final GoalMapper goalMapper;
    private final IsaAccountService isaAccountService;


    //1.  목표 생성
    public void createGoal(Long memberId ,Goal goal) {
        //1. isa 할당한 금액 금액 가져오기
        List<IsaAccountProductRes> isaProducts = isaAccountService.findAllByMemberIdAndGoalId(
                memberId, goal.getObjectId()
        );

        //2. isa 금액 합산
        long isaAmount = isaProducts.stream()
                        .mapToLong(p->p.getPresentAmount().longValue())
                                .sum();

        //3.예적금 금액 -> 임시 0 (todo)
        long savingAmount = 0;

        //4.초기 자산 설정 (isa + 예적금)
        long startAmount = isaAmount+savingAmount;

        goal.setMemberId(memberId);
        goal.setStartAmount(startAmount);

        //5.목표 달성률 계산
        if(goal.getTargetAmount()!=null&&goal.getTargetAmount()>0){
            int goalRate = (int)((double)startAmount*100/goal.getTargetAmount());
            goal.setGoalRate(goalRate);
        }
        else {
            goal.setGoalRate(0);
        }

        int rowsAffected = goalMapper.insertGoal(goal);
        if (rowsAffected == 0) {
            throw new BusinessException(ResponseCode.GOAL_CREATE_FAILED);
        }

    }


    @Transactional(readOnly = true)
    public Optional<Goal> getGoal(Long memberId,Long goalId ){
        return Optional.ofNullable(goalMapper.selectGoalById(goalId,memberId));
    }

    //모든 목표 조회
    @Transactional(readOnly = true)
    public List<Goal> getGoals(Long memberId) {
        return goalMapper.selectAllGoals(memberId);
    }


    public void updateGoal(Long objectId, Long memberId ,Goal updatedGoal) {
        List<IsaAccountProductRes> isaProducts = isaAccountService.findAllByMemberIdAndGoalId(
                memberId, objectId
        );
        long isaAmount = isaProducts.stream()
                .mapToLong(p -> p.getPresentAmount().longValue())
                .sum();
        long savingAmount = 0;
        long startAmount = isaAmount + savingAmount;

        updatedGoal.setStartAmount(startAmount);
        updatedGoal.setObjectId(objectId);
        updatedGoal.setMemberId(memberId);


        if (updatedGoal.getTargetAmount() != null && updatedGoal.getTargetAmount() > 0) {
            int goalRate = (int) ((double) startAmount * 100 / updatedGoal.getTargetAmount());
            updatedGoal.setGoalRate(goalRate);
        } else {
            updatedGoal.setGoalRate(0);
        }


        int rowsAffected = goalMapper.updateGoal(updatedGoal);
        if (rowsAffected == 0) {
            throw new BusinessException(ResponseCode.GOAL_UPDATE_FAILED);
        }
    }

    //목표 삭제
    public int deleteGoal(Long objectId, Long memberId) {
        return goalMapper.deleteGoal(objectId, memberId);
    }

    public List<GoalAccountRateResponse> getGoalAccountRates(Long goalId) {
        Long targetAmount = goalMapper.getTargetAmountByGoalId(goalId);

        if (targetAmount == null) {
            throw new IllegalArgumentException("해당 goalId의 목표 금액이 존재하지 않습니다: " + goalId);
        }

        List<Map<String, Object>> rows = goalMapper.getGoalAccountRates(goalId);

        return rows.stream()
            .map(row -> {
                String bankCode = (String) row.get("bankCode");
                String bankName = Bank.getNameByCode(bankCode);

                long accountBalance = ((Number) row.get("accountBalance")).longValue();
                int accountAllocatedRate = ((Number) row.get("allocatedRate")).intValue();

                double progress = (accountBalance * (accountAllocatedRate / 100.0)) / targetAmount * 100;

                return new GoalAccountRateResponse(bankName, progress);
            })
            .collect(Collectors.toList());
    }

}
