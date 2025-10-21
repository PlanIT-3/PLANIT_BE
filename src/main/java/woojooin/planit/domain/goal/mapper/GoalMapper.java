package woojooin.planit.domain.goal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.goal.api.dto.res.GoalDepositAmountRes;
import woojooin.planit.domain.goal.domain.vo.Goal; // 변경된 Goal VO 임포트
import woojooin.planit.domain.goal.domain.vo.GoalProgress;

import woojooin.planit.domain.goal.domain.dto.DepositAccountDto;
import woojooin.planit.domain.goal.domain.dto.IsaProductDto;
import woojooin.planit.domain.goal.api.dto.res.IsaAccountProductRes;

import java.util.List;

@Mapper
public interface GoalMapper {

    int insertGoal(Goal goal);

    Goal selectGoalById(@Param("memberId") Long memberId,@Param("goalId") Long goalId);

    List<Goal> selectAllGoals(Long memberId);

    int updateGoal(Goal goal);

    int deleteGoal(@Param("goalId") Long goalId, @Param("memberId") Long memberId);

//    int softDeleteActionsByGoalId(@Param("goalId") Long goalId, @Param("memberId") Long memberId);

    List<IsaAccountProductRes> findAllocatedIsaByGoal(@Param("memberId") Long memberId, @Param("goalId") Long goalId);

    List<GoalDepositAmountRes> findAllocatedDepositByGoal(@Param("memberId") Long memberId, @Param("goalId") Long goalId);

    List<DepositAccountDto> selectDepositAccountsByGoalId(Long goalId);

    List<IsaProductDto> selectIsaProductsByGoalId(Long goalId);

    // 목표 금액 조회
    Long selectGoalTargetAmount(Long goalId);

	  List<GoalProgress> selectGoalProgressByGoalId(@Param("goalId") Long goalId);

	  List<GoalProgress> selectDailyGoalProgressLast6Months(@Param("goalId") Long goalId);

      Goal selectGoalWithAllDetails(@Param("goalId") Long goalId);

      Goal selectById(@Param("goalId") Long goalId);
}
