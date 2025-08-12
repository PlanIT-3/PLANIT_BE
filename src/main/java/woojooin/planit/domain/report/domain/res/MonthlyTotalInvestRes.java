package woojooin.planit.domain.report.domain.res;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyTotalInvestRes {
    private List<Timestamp> date;
    private List<BigDecimal> monthlyTotalAmount;
    private List<BigDecimal> monthlyValuationAmount;
}
