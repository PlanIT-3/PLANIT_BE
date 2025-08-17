package woojooin.planit.domain.rebalance.dto.res;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.rebalance.enums.InvestType;
import woojooin.planit.domain.rebalance.vo.Rebalance;

@Data
@NoArgsConstructor
public class RebalancingInfo {

	private String goalName;
	private List<Info> rebalanceInfo = new ArrayList<>();

	@Data
	@NoArgsConstructor
	public static class Info {
		private String productCode;
		private Long memberProductId;
		private Long goalId;
		private String comment;
		private BigDecimal expectedReturnRate;
		private String previousProductName;
		private String nextProductName;
		private InvestType investType;

		public static Info from(Rebalance r) {
			Info i = new Info();
			i.productCode = r.getProductCode();
			i.memberProductId = r.getMemberProductId();
			i.goalId = r.getGoalId();
			i.comment = r.getComment();
			i.expectedReturnRate = r.getExpectedReturnRate();
			i.previousProductName = r.getPreviousProductName();
			i.nextProductName = r.getNextProductName();
			i.investType = r.getInvestType();
			return i;
		}
	}

	public void addInfo(Rebalance rebalance) {
		if (rebalance == null)
			return;
		this.rebalanceInfo.add(Info.from(rebalance));
	}
}
