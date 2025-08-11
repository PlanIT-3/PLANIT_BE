package woojooin.planit.domain.openAi.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvestReportRes {
    private List<Double> recommendedInvestmentAmount;
    private String investmentAdvice;
}
