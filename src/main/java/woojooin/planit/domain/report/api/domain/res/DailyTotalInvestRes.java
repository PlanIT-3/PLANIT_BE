package woojooin.planit.domain.report.api.domain.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyTotalInvestRes {
    private List<Timestamp> date;
    private List<BigDecimal> dailyTotalAmount;
    private List<BigDecimal> dailyValuationAmount;
}
