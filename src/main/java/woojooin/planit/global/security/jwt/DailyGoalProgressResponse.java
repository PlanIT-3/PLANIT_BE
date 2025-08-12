package woojooin.planit.global.security.jwt;

import java.time.LocalDateTime;

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
	private LocalDateTime createdAt;

	public static DailyGoalProgressResponse fromEntity(GoalProgress entity) {
		return new DailyGoalProgressResponse(
			(long) entity.getGoalId(),
			entity.getIsaProgress(),
			entity.getDepositProgress(),
			entity.getCreatedAt()
		);
	}
}