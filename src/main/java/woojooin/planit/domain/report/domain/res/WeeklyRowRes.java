package woojooin.planit.domain.report.domain.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyRowRes {
    private Timestamp date;
    private BigDecimal weeklyInvestTotal;
    private BigDecimal weeklyValuationTotal;
}
