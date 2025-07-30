package woojooin.planit.domain.object.isa.dto.req;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsaAccountProductRegisterListReq {

	@NotNull(message = "상품 목록은 필수입니다.")
	@NotEmpty(message = "등록할 상품 목록이 비어있습니다.")
	@Valid
	private List<IsaAccountProductRegisterReq> isaAccountProductRegisterReqs;
}
