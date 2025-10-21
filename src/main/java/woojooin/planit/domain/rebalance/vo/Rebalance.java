package woojooin.planit.domain.rebalance.vo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.goal.domain.vo.Goal;
import woojooin.planit.domain.member.domain.MemberProduct;
import woojooin.planit.domain.product.domain.Product;
import woojooin.planit.domain.rebalance.enums.InvestType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rebalance {

	private Goal goal;
	private Product product;
	private MemberProduct memberProduct;

	private Long rebalanceId;
	private String productCode;
	private Long memberProductId;
	private Long goalId;
	private String comment;
	private BigDecimal expectedReturnRate;
	private String previousProductName;
	private String nextProductName;
	private InvestType investType;
}

