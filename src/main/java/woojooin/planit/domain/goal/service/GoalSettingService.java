package woojooin.planit.domain.goal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import woojooin.planit.domain.goal.domain.Goal;
import woojooin.planit.domain.goal.dto.GoalDetailResponseDto;
import woojooin.planit.domain.goal.mapper.GoalMapper;
import woojooin.planit.domain.object.isa.dto.res.IsaAccountProductRes;
import woojooin.planit.domain.object.isa.service.IsaAccountService;
import woojooin.planit.global.exception.BusinessException;
import woojooin.planit.global.response.ResponseCode;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalSettingService {
    private final GoalMapper goalMapper;
    private final IsaAccountService isaAccountService;

    public void createGoal(Long memberId ,Goal goal) {
        //1. 기본 세팅
        goal.setMemberId(memberId);
        goal.setStartAmount(0L);
        goal.setGoalRate(0);

        //2. insert 먼저 수행 -> objectID 생성
        int rowAffected = goalMapper.insertGoal(goal);
        if(rowAffected == 0) {
            throw new BusinessException(ResponseCode.GOAL_CREATE_FAILED);
        }

        //3 objectID 기반 isa 조회
        Long objectId = goal.getObjectId();
        List<IsaAccountProductRes> isaProducts = isaAccountService.findAllByMemberIdAndGoalId(
                memberId, goal.getObjectId()
        );

        long isaAmount = isaProducts.stream()
                .mapToLong(p->p.getPresentAmount().longValue())
                .sum();

        //예적금 금액 -> 임시 0 (todo)
        long savingAmount = 0;
        long startAmount = isaAmount+savingAmount;
        goal.setStartAmount(startAmount);


        if(goal.getTargetAmount()!=null&&goal.getTargetAmount()>0){
            int goalRate = (int)((double)startAmount*100/goal.getTargetAmount());
            goal.setGoalRate(goalRate);
        }
        goalMapper.updateGoal(goal);
    }


    @Transactional(readOnly = true)
    public GoalDetailResponseDto getGoalDetail(Long memberId, Long goalId ){
        Goal goal = Optional.ofNullable(goalMapper.selectGoalById(goalId, memberId))
                .orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));

        List<IsaAccountProductRes> isaAccounts =
                isaAccountService.findAllByMemberIdAndGoalId(memberId, goalId);

        //todo 예적금 list

        return GoalDetailResponseDto.builder()
                .objectName(goal.getObjectName())
                .targetAmount(goal.getTargetAmount())
                .totalAmount(goal.getStartAmount())
                .goalRate(goal.getGoalRate())
                .endDate(goal.getEndDate())
                .isaAccounts(isaAccounts)
                //todo 예적금 연결
                .build();
    }


    @Transactional(readOnly = true)
    public List<GoalDetailResponseDto> getAllGoals(Long memberId) {
        List<Goal> goals = goalMapper.selectAllGoals(memberId);
        return goals.stream()
                .map(goal -> {
                    List<IsaAccountProductRes> isaAccounts =
                            isaAccountService.findAllByMemberIdAndGoalId(memberId, goal.getObjectId());

                    return GoalDetailResponseDto.builder()
                            .objectName(goal.getObjectName())
                            .targetAmount(goal.getTargetAmount())
                            .totalAmount(goal.getStartAmount())
                            .goalRate(goal.getGoalRate())
                            .endDate(goal.getEndDate())
                            .isaAccounts(isaAccounts)
                            .build();
                })
                .collect(Collectors.toList());
    }


    public GoalDetailResponseDto updateGoal(Long objectId, Long memberId ,Goal updatedGoal) {
        List<IsaAccountProductRes> isaProducts = isaAccountService.findAllByMemberIdAndGoalId(
                memberId, objectId
        );
        long isaAmount = isaProducts.stream()
                .mapToLong(p -> p.getPresentAmount().longValue())
                .sum();
        long savingAmount = 0; //todo 예적금 확장
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
        return GoalDetailResponseDto.builder()
                .objectName(updatedGoal.getObjectName())
                .targetAmount(updatedGoal.getTargetAmount())
                .totalAmount(updatedGoal.getStartAmount())
                .goalRate(updatedGoal.getGoalRate())
                .endDate(updatedGoal.getEndDate())
                .isaAccounts(isaProducts)
                .build();
    }

    //목표 삭제
    public int deleteGoal(Long objectId, Long memberId) {
        return goalMapper.deleteGoal(objectId, memberId);
    }
}