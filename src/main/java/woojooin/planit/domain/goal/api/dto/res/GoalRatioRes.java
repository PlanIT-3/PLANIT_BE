package woojooin.planit.domain.goal.api.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalRatioRes {
    private String objectName;
    private Long targetAmount;
    private BigDecimal ratio;
}