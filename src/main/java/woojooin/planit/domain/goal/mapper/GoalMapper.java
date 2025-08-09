package woojooin.planit.domain.goal.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.goal.domain.Goal; // 변경된 Goal VO 임포트

import java.util.List;
import java.util.Map;

@Mapper
public interface GoalMapper {
	int insertGoal(Goal goal);

	Goal selectGoalById(@Param("objectId") Long objectId, @Param("memberId") Long memberId);

	List<Goal> selectAllGoals(Long memberId);

	int updateGoal(Goal goal);

	int deleteGoal(@Param("objectId") Long objectId, @Param("memberId") Long memberId);

	@MapKey("bankCode")
	List<Map<String, Object>> getGoalAccountRates(@Param("goalId")Long goalId);

	Long getTargetAmountByGoalId(Long goalId);

}
