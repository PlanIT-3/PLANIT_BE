package woojooin.planit.domain.report.api.domain.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyRowRes {
    private Timestamp date;
    private BigDecimal dailyInvestTotal;
    private BigDecimal dailyValuationTotal;
}
