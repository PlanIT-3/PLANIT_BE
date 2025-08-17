package woojooin.planit.domain.goal.goalAccount.dto.res;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GoalAccountRes {
    private List<GoalDepositRes> goalDepositList;
    private List<GoalIsaRes> goalIsaList;
}
