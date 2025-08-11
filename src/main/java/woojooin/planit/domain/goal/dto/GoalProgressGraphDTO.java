package woojooin.planit.domain.goal.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.goal.domain.GoalProgress;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalProgressGraphDTO {
	private LocalDateTime progressDate;
	private double isaProgress;
	private double depositProgress;

	public static GoalProgressGraphDTO fromEntity(GoalProgress entity) {
		return new GoalProgressGraphDTO(
			entity.getProgressDate(),
			entity.getIsaProgress(),
			entity.getDepositProgress()
		);
	}
}