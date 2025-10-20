package woojooin.planit.domain.goal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.goal.domain.Goal;
import woojooin.planit.domain.goal.domain.GoalProgress;
import woojooin.planit.domain.goal.dto.DepositAccountDto;
import woojooin.planit.domain.goal.dto.IsaProductDto;
import woojooin.planit.domain.goal.dto.res.GoalDepositResponse;
import woojooin.planit.domain.goal.isa.dto.res.IsaAccountProductRes;

@Mapper
public interface GoalMapper {

	Goal findById(@Param("id") long id);

	int insertGoal(Goal goal);

	Goal selectGoalById(@Param("memberId") Long memberId, @Param("goalId") Long goalId);

	List<Goal> selectAllGoals(Long memberId);

	int updateGoal(Goal goal);

	int deleteGoal(@Param("goalId") Long goalId, @Param("memberId") Long memberId);

	int softDeleteActionsByGoalId(@Param("goalId") Long goalId, @Param("memberId") Long memberId);

	List<IsaAccountProductRes> findAllocatedIsaByGoal(@Param("memberId") Long memberId, @Param("goalId") Long goalId);

	List<GoalDepositResponse> findAllocatedDepositByGoal(@Param("memberId") Long memberId,
		@Param("goalId") Long goalId);

	List<DepositAccountDto> selectDepositAccountsByGoalId(Long goalId);

	List<IsaProductDto> selectIsaProductsByGoalId(Long goalId);

	// 목표 금액 조회
	Long selectGoalTargetAmount(Long goalId);

	List<GoalProgress> selectGoalProgressByGoalId(@Param("goalId") Long goalId);

	List<GoalProgress> selectDailyGoalProgressLast6Months(@Param("goalId") Long goalId);

}
