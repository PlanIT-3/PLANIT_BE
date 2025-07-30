package woojooin.planit.domain.goal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import woojooin.planit.domain.goal.domain.Goal; // 변경된 Goal VO 임포트

import java.util.List;

@Mapper

public interface GoalMapper {
    int insertGoal(Goal goal);
    //조회
    Goal selectGoalById(@Param("goalId") Long goalId, @Param("userId") Long userId);
    List<Goal> selectAllGoals(Long userId);
    int updateGoal(Goal goal);
    int deleteGoal(@Param("goalId") Long goalId, @Param("userId") Long userId);

}
