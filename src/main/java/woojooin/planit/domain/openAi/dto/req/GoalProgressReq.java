package woojooin.planit.domain.openAi.dto.req;

import lombok.Data;

import java.util.List;

@Data
public class GoalProgressReq {
    private String riskProfile;
    private List<Double> isaProgress;
    private List<Double> depositProgress;
}
