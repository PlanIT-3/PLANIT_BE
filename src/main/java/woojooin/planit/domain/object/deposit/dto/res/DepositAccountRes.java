package woojooin.planit.domain.object.deposit.dto.res;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositAccountRes {

	private Long memberAccountId;

	private String accountNumber;

	private String accountName;

	private BigDecimal presentAmount;

	private boolean checked;

	private Integer allocatedAmount;  // 현재 할당된 총액
	
	private Integer remainingAmount;  // 할당 가능한 잔여액
}