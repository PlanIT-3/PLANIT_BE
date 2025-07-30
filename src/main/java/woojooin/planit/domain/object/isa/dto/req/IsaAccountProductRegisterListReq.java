package woojooin.planit.domain.object.isa.dto.req;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsaAccountProductRegisterListReq {

	private List<IsaAccountProductRegisterReq> isaAccountProductRegisterReqs;
}
