package woojooin.planit.domain.object.deposit.dto.req;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositProductRegisterListReq {

	@NotEmpty(message = "예적금 상품 목록은 비어있을 수 없습니다.")
	@Valid
	private List<DepositProductRegisterReq> depositProductRegisterReqs;
}