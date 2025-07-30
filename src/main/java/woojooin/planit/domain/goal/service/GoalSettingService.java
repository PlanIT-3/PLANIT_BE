package woojooin.planit.domain.goal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import woojooin.planit.domain.goal.domain.Goal;
import woojooin.planit.domain.goal.mapper.GoalMapper;
import woojooin.planit.domain.object.isa.dto.res.IsaAccountProductRes;
import woojooin.planit.domain.object.isa.service.IsaAccountService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoalSettingService {
    private final GoalMapper goalMapper;
    private final IsaAccountService isaAccountService;


    //1.  목표 생성
    public int createGoal(Long memberId ,Goal goal) {
        //1. isa 할당한 금액 금액 가져오기
        List<IsaAccountProductRes> isaProducts = isaAccountService.findAllByMemberIdAndObjectId(
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


        return goalMapper.insertGoal(goal);
    }

    //  목표 하나 조회
    @Transactional(readOnly = true)
    public Optional<Goal> getGoal(Long memberId,Long goalId ){
        return Optional.ofNullable(goalMapper.selectGoalById(goalId,memberId));
    }

    //모든 목표 조회
    @Transactional(readOnly = true)
    public List<Goal> getGoals(Long memberId) {
        return goalMapper.selectAllGoals(memberId);
    }

    //목표 수정
    public int updateGoal(Long objectId, Long memberId ,Goal updatedGoal) {
        List<IsaAccountProductRes> isaProducts = isaAccountService.findAllByMemberIdAndObjectId(
                memberId, objectId
        );
        long startAmount = isaProducts.stream()
                .mapToLong(p -> p.getPresentAmount().longValue())
                .sum();

        updatedGoal.setObjectId(objectId);
        updatedGoal.setMemberId(memberId);
        updatedGoal.setStartAmount(startAmount);
        if (updatedGoal.getTargetAmount() != null && updatedGoal.getTargetAmount() > 0) {
            int goalRate = (int) ((double) startAmount * 100 / updatedGoal.getTargetAmount());
            updatedGoal.setGoalRate(goalRate);
        } else {
            updatedGoal.setGoalRate(0);
        }
        int rowAffected = goalMapper.updateGoal(updatedGoal);
        if(rowAffected == 0){
            throw new IllegalArgumentException("Goal not found or access denied for ID: " + objectId);}
        return rowAffected;
    }

    //목표 삭제
    public int deleteGoal(Long objectId, Long memberId) {
        Goal goal = goalMapper.selectGoalById(objectId,memberId);
        if(goal == null){
            throw new IllegalArgumentException("Goal not found or access denied for ID: " + objectId);
        }
        return goalMapper.deleteGoal(objectId,memberId);
    }
}
