package woojooin.planit.domain.openAi.dto.req;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InvestReportReq {
    private List<BigDecimal> monthlyInvestmentAmount;
    private List<BigDecimal> monthlyEvaluationAmount;
    private List<BigDecimal> etfReturnRate;
    private String riskProfile;
}
