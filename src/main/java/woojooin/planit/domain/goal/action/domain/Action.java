package woojooin.planit.domain.goal.action.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Action {

	private Long actionId;

	private Long goalId;

	private String accountNumber;

	private Long accountId;

	private Integer accountAllocatedRate;

	private String accountType;

	private Integer amount;


	private ActionType actionType;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}