package woojooin.planit.domain.goal.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsaAccountProductEditReq {

	private Long goalId;

	private Long memberProductId;

	private String accountType = "ISA";

	private boolean checked;
}