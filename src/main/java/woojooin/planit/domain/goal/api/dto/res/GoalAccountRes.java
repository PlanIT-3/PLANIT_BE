package woojooin.planit.domain.goal.api.dto.res;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GoalAccountRes {
    private List<GoalDepositAmountRes> goalDepositList;
    private List<GoalIsaRes> goalIsaList;
}
