package woojooin.planit.domain.goal.deposit.dto.req;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositAccountRegisterListReq {

	@NonNull
	private List<DepositAccountRegisterReq> depositAccountRegisterReqs;
}