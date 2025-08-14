package woojooin.planit.domain.goal.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositAccountDto {
	private String bankCode;         // 은행 코드
	private BigDecimal accountBalance; // 잔액
	private Integer allocatedRate;     // 할당 비율 (%)
}