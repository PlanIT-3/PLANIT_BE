package woojooin.planit.domain.object.deposit.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import woojooin.planit.domain.object.action.domain.ActionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositAccountRegisterReq {

	@NonNull
	private Long objectId;

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

	private ActionType actionType = ActionType.DEPOSIT;
}