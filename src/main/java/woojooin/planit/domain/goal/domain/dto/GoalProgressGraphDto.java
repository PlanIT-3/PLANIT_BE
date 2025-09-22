package woojooin.planit.domain.goal.domain.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.goal.domain.vo.GoalProgress;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalProgressGraphDto {
	private LocalDateTime progressDate;
	private double isaProgress;
	private double depositProgress;

	public static GoalProgressGraphDto fromEntity(GoalProgress entity) {
		return new GoalProgressGraphDto(
			entity.getProgressDate(),
			entity.getIsaProgress(),
			entity.getDepositProgress()
		);
	}
}