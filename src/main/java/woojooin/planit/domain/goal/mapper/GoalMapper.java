package woojooin.planit.domain.goal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import woojooin.planit.domain.goal.domain.Goal; // 변경된 Goal VO 임포트

import woojooin.planit.domain.object.deposit.dto.res.DepositAccountRes;
import woojooin.planit.domain.object.isa.dto.res.IsaAccountProductRes;

import java.util.List;

@Mapper

public interface GoalMapper {
    int insertGoal(Goal goal);
    Goal selectGoalById(@Param("objectId") Long objectId, @Param("memberId") Long memberId);
    List<Goal> selectAllGoals(Long memberId);
    int updateGoal(Goal goal);
    int deleteGoal(@Param("objectId") Long objectId, @Param("memberId") Long memberId);
    List<IsaAccountProductRes> findAllocatedIsaByGoal(@Param("memberId") Long memberId, @Param("goalId") Long goalId);
    List<DepositAccountRes> findAllocatedDepositByGoal(@Param("memberId") Long memberId,@Param("goalId") Long goalId);
}
