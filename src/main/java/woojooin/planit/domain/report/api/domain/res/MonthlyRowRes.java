package woojooin.planit.domain.report.api.domain.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyRowRes {
    private Timestamp date;
    private BigDecimal monthlyInvestTotal;
    private BigDecimal monthlyValuationTotal;
}
