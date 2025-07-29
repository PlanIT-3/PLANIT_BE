package woojooin.planit.domain.account.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Account {

	private Long accountId;

	private Long memberId;

	private String accountName;
	private String accountNumber;
	private String accountCurrency;

	private BigDecimal accountBalance;
	private BigDecimal accountDeposit;
	private BigDecimal earningsRate;
	private BigDecimal accountInvestedCost;

	private LocalDateTime lastTranDate;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
