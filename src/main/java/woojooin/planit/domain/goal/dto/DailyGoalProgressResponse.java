package woojooin.planit.domain.goal.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.goal.domain.GoalProgress;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyGoalProgressResponse {
	private Long goalId;
	private double isaProgress;
	private double depositProgress;
	private String createdAt;

	public static DailyGoalProgressResponse fromEntity(GoalProgress entity) {
		String formattedDate = entity.getCreatedAt() != null 
			? entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
			: null;
		
		return new DailyGoalProgressResponse(
			(long) entity.getGoalId(),
			entity.getIsaProgress(),
			entity.getDepositProgress(),
			formattedDate
		);
	}
}