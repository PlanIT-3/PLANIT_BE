package woojooin.planit.global.util.calc.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RebalanceChoice {
	public enum Action {BUY, SELL, HOLD}

	private String goalName;
	private Action action;              // BUY / SELL / HOLD
	private String mpName;              // 대상 MP 이름
	private String mpCode;              // 대상 MP 코드(옵션)
	private BigDecimal tradeAmount;     // 체결금액(원, 절대값)
	private BigDecimal targetIsaAmount; // 목표 ISA 금액
	private BigDecimal mpTotal;         // 현재 MP 총액
	private BigDecimal deposit;         // 현재 예적금(입력값)
	private String reason;              // 사유
}
