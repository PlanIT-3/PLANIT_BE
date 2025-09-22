package woojooin.planit.domain.goal.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import woojooin.planit.domain.goal.domain.enums.ActionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositAccountEditReq {

	@NonNull
	private Long goalId;

	@NonNull
	private Long memberAccountId;

	@NonNull
	private String accountNumber;

	private String accountType;

	@NonNull
	private Integer amount;

	@NonNull
	private Integer allocatedRate;

	@NonNull
	private Integer accountAllocatedRate;

	private boolean checked;

	private ActionType actionType = ActionType.DEPOSIT;
}