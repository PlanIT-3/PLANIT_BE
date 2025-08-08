package woojooin.planit.domain.goal.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalProgressGraphDTO {
	private LocalDateTime progressDate;
	private double isaProgress;
	private double depositProgress;

}

