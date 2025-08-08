package woojooin.planit.domain.account.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Account {

	private Long accountId;           // bigint AI PK

	private Long memberId;            // bigint

	private String accountName;       // varchar(255)
	private String accountNumber;     // varchar(50)
	private String accountCurrency;   // varchar(10)

	private BigDecimal accountBalance;      // decimal(20,2)
	private BigDecimal accountDeposit;      // decimal(20,2)
	private BigDecimal earningsRate;        // decimal(5,4)
	private BigDecimal accountInvestedCost; // decimal(20,2)

	private LocalDateTime lastTranDate;     // timestamp
	private LocalDateTime accountStartDate; // timestamp - 계좌 실제 시작일
	private LocalDateTime accountEndDate;   // timestamp - 계좌 만기일

	private Boolean isDeleted;              // tinyint(1)
	private Boolean isIntegrated;           // tinyint(1) - CODEF 연동 여부
	private int bankCode;

	private LocalDateTime createdAt;        // timestamp
	private LocalDateTime updatedAt;        // timestamp
}
