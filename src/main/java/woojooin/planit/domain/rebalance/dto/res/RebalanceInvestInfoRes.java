package woojooin.planit.domain.rebalance.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RebalanceInvestInfoRes {
    private String itemName;
    private BigDecimal quantity;
    private BigDecimal valuationAmount;
    private BigDecimal earningsRate;
    private BigDecimal totalValuationAmount;
}
