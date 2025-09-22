package woojooin.planit.domain.goal.api.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalAccountRateRes {
	private String bankName;   // 은행 이름
	private double progressRate;  // 목표 대비 진행률(%)
}
