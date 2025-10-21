package woojooin.planit.domain.goal.api.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalRatioListRes {
    private BigDecimal totalBalance;
    private List<GoalRatioRes> goalRatios;
}