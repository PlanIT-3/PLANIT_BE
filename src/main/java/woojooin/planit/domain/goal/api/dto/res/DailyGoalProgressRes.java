package woojooin.planit.domain.goal.api.dto.res;

import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.goal.domain.vo.GoalProgress;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyGoalProgressRes {
	private Long goalId;
	private double isaProgress;
	private double depositProgress;
	private String createdAt;

	public static DailyGoalProgressRes fromEntity(GoalProgress entity) {
		String formattedDate = entity.getCreatedAt() != null 
			? entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
			: null;
		
		return new DailyGoalProgressRes(
			(long) entity.getGoalId(),
			entity.getIsaProgress(),
			entity.getDepositProgress(),
			formattedDate
		);
	}
}