package woojooin.planit.domain.report.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.account.domain.Account;
import woojooin.planit.domain.member.domain.Member;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositTaxSavingHistory {
    private Long depositTaxSavingHistoryId;
    private Long memberId;
    private Long accountId;
    private String quarter;
    private BigDecimal interestIncome;
    private BigDecimal incomeTax;
    private BigDecimal localIncomeTax;
    private BigDecimal totalTax;
    private BigDecimal netIncome;
    private Member member;
    private Account account;
}
