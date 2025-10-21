package woojooin.planit.domain.report.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.member.domain.Member;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IsaTaxSavingHistory {
    private Long isaTaxSavingHistoryId;
    private Long memberId;
    private String quarter;
    private BigDecimal isaProfit;
    private BigDecimal generalTax;
    private BigDecimal taxSaved;
    private Member member;
}
