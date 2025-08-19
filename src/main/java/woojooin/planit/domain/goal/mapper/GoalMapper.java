package woojooin.planit.domain.goal.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.goal.domain.Goal; // 변경된 Goal VO 임포트
import woojooin.planit.domain.goal.domain.GoalProgress;

import woojooin.planit.domain.goal.dto.DepositAccountDto;
import woojooin.planit.domain.goal.dto.IsaProductDto;
import woojooin.planit.domain.goal.dto.res.GoalDepositResponse;
import woojooin.planit.domain.goal.isa.dto.res.IsaAccountProductRes;
import woojooin.planit.domain.goal.deposit.dto.res.DepositAccountRes;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoalMapper {

    int insertGoal(Goal goal);

    Goal selectGoalById(@Param("memberId") Long memberId,@Param("goalId") Long goalId);

    List<Goal> selectAllGoals(Long memberId);

    int updateGoal(Goal goal);

    int deleteGoal(@Param("goalId") Long goalId, @Param("memberId") Long memberId);

    int softDeleteActionsByGoalId(@Param("goalId") Long goalId, @Param("memberId") Long memberId);

    List<IsaAccountProductRes> findAllocatedIsaByGoal(@Param("memberId") Long memberId, @Param("goalId") Long goalId);

    List<GoalDepositResponse> findAllocatedDepositByGoal(@Param("memberId") Long memberId, @Param("goalId") Long goalId);

    List<DepositAccountDto> selectDepositAccountsByGoalId(Long goalId);

    List<IsaProductDto> selectIsaProductsByGoalId(Long goalId);

    // 목표 금액 조회
    Long selectGoalTargetAmount(Long goalId);

	  List<GoalProgress> selectGoalProgressByGoalId(@Param("goalId") Long goalId);

	  List<GoalProgress> selectDailyGoalProgressLast6Months(@Param("goalId") Long goalId);

}
