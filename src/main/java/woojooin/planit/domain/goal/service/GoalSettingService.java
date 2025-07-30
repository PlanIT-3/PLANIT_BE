package woojooin.planit.domain.goal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import woojooin.planit.domain.goal.domain.Goal;
import woojooin.planit.domain.goal.mapper.GoalMapper;

import java.util.List;
import java.util.Optional;

@Service
public class GoalSettingService {
    private final GoalMapper goalMapper;

    @Autowired
    public GoalSettingService(GoalMapper goalMapper) {
        this.goalMapper = goalMapper;
    }

    //1.  목표 생성
    public int createGoal(Long currentUserID ,Goal goal) {
        goal.setUserId(currentUserID);
        return goalMapper.insertGoal(goal);
    }

    //  목표 하나 조회
    @Transactional(readOnly = true)
    public Optional<Goal> getGoal(Long currentUserID,Long goalId ){
        return Optional.ofNullable(goalMapper.selectGoalById(goalId,currentUserID));
    }

    //모든 목표 조회
    @Transactional(readOnly = true)
    public List<Goal> getGoals(Long currentUserID) {
        return goalMapper.selectAllGoals(currentUserID);
    }

    //목표 수정
    public int updateGoal(Long goalId, Long currentUserID ,Goal updatedGoal) {
        updatedGoal.setGoalId(goalId);//pathvariable로 받은  goalid를 Goal객체에
        updatedGoal.setUserId(currentUserID);// 사용자 id를 goal객체에
        int rowAffected = goalMapper.updateGoal(updatedGoal);
        if(rowAffected == 0){
            throw new IllegalArgumentException("Goal not found or access denied for ID: " + goalId);}
        return rowAffected;
    }

    //목표 삭제
    public int deleteGoal(Long goalId, Long currentUserID) {
        Goal goal = goalMapper.selectGoalById(goalId,currentUserID);
        if(goal == null){
            throw new IllegalArgumentException("Goal not found or access denied for ID: " + goalId);
        }
        return goalMapper.deleteGoal(goalId,currentUserID);
    }
}
