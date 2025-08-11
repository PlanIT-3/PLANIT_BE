package woojooin.planit.domain.goal.deposit.dto.res;

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

	private BigDecimal  allocatedAmount;  // 현재 할당된 총액
	
	private BigDecimal  remainingAmount;  // 할당 가능한 잔여액
}