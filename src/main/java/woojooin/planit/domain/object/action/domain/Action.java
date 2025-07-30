package woojooin.planit.domain.object.action.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Action {

	private Long actionId;

	private Long objectId;

	private String accountNumber;

	private Long memberProductId;

	private Integer allocatedRate;
	private Integer accountAllocatedRate;

	private String accountType;

	private Integer amount;


	private ActionType actionType;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}