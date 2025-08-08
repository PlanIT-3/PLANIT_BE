package woojooin.planit.domain.goal.isa.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsaAccountProductRegisterReq {

	@NonNull
	private Long goalId;

	@NonNull
	private Long memberProductId;

	private String accountType = "ISA";
}
