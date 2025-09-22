package woojooin.planit.global.util.calc;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import woojooin.planit.domain.goal.domain.vo.Goal;
import woojooin.planit.domain.member.domain.MemberProduct;
import woojooin.planit.global.util.calc.dto.RebalanceChoice;

public class RebalanceCalc {

	/**
	 * deposit: 현재 목표에 배정된 현금/예적금 금액
	 * mpList:  정확히 2개 MP (valuationAmount, earningsRate 등 세팅)
	 *
	 * 규칙:
	 * - 기준자산 = min(deposit + mpTotal, goal.targetAmount)
	 * - isa 목표금액 = 기준자산 * (goal.isaRate / 100)
	 * - delta = 목표금액 - mpTotal
	 * - delta>0 매수: 수익률 높은 MP 하나에만, trade = min(delta, deposit)
	 * - delta<0 매도: 수익률 높은 MP 하나에만, trade = min(|delta|, 그 MP의 valuationAmount)
	 */
	public static RebalanceChoice decide(Goal goal,
		BigDecimal deposit,
		List<MemberProduct> mpList) {

		Objects.requireNonNull(goal, "goal");
		Objects.requireNonNull(deposit, "deposit");
		Objects.requireNonNull(mpList, "mpList");
		if (mpList.size() != 2)
			throw new IllegalArgumentException("mpList는 정확히 2개여야 합니다.");

		BigDecimal mp1Val = nvl(mpList.get(0).getValuationAmount());
		BigDecimal mp2Val = nvl(mpList.get(1).getValuationAmount());
		BigDecimal mpTotal = mp1Val.add(mp2Val);

		// 현재 총자산 및 기준자산(목표 상한 캡)
		BigDecimal V = deposit.add(mpTotal);
		BigDecimal targetAmount = nvl(BigDecimal.valueOf(goal.getTargetAmount()));
		if (targetAmount.compareTo(BigDecimal.ZERO) <= 0) {
			// targetAmount가 0 또는 미설정이면 전체 포트폴리오 기준으로 간주
			targetAmount = V;
		}
		BigDecimal base = V.min(targetAmount);

		// 목표 ISA 금액
		BigDecimal isaRatePct = BigDecimal.valueOf(goal.getIsaRate() == null ? 0 : goal.getIsaRate())
			.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
		BigDecimal targetIsaAmount = base.multiply(isaRatePct).setScale(0, RoundingMode.HALF_UP);

		// 부족/초과 금액
		BigDecimal delta = targetIsaAmount.subtract(mpTotal); // +면 매수, -면 매도

		// 수익률 높은 MP 선택
		MemberProduct winner = pickHigherPerformer(mpList);

		// HOLD 조건: 거래할 돈도/물량도 없거나 delta=0
		if (delta.compareTo(BigDecimal.ZERO) == 0) {
			return hold("목표 비중과 동일", winner, targetIsaAmount, mpTotal, deposit);
		}

		if (delta.signum() > 0) {
			// 매수: deposit 한도 내
			BigDecimal buy = delta.min(deposit).max(BigDecimal.ZERO).setScale(0, RoundingMode.HALF_UP);
			if (buy.compareTo(BigDecimal.ZERO) == 0) {
				return hold("현금 부족으로 매수 불가", winner, targetIsaAmount, mpTotal, deposit);
			}
			return RebalanceChoice.builder()
				.action(RebalanceChoice.Action.BUY)
				.mpName(winner.getItemName())
				.mpCode(winner.getItemCode())
				.tradeAmount(buy)
				.targetIsaAmount(targetIsaAmount)
				.mpTotal(mpTotal)
				.deposit(deposit)
				.reason("ISA 목표금액 미달 → 수익률 높은 종목 매수")
				.build();
		} else {
			// 매도: 선택 종목의 평가금액 한도 내
			BigDecimal winnerVal = nvl(winner.getValuationAmount());
			BigDecimal sellNeeded = delta.abs();
			BigDecimal sell = sellNeeded.min(winnerVal).setScale(0, RoundingMode.HALF_UP);
			if (sell.compareTo(BigDecimal.ZERO) == 0) {
				return hold("선택 종목 평가금액이 없어 매도 불가", winner, targetIsaAmount, mpTotal, deposit);
			}
			return RebalanceChoice.builder()
				.action(RebalanceChoice.Action.SELL)
				.mpName(winner.getItemName())
				.mpCode(winner.getItemCode())
				.tradeAmount(sell)
				.targetIsaAmount(targetIsaAmount)
				.mpTotal(mpTotal)
				.deposit(deposit)
				.reason("ISA 목표금액 초과 → 수익률 높은 종목 매도")
				.build();
		}
	}

	// 수익률이 더 높은 MP 선택(우선순위: earningsRate → (valuationPl/purchaseAmount) → valuationPl)
	private static MemberProduct pickHigherPerformer(List<MemberProduct> list) {
		return list.stream().max(Comparator.comparing(RebalanceCalc::performanceScore)).orElse(list.get(0));
	}

	private static BigDecimal performanceScore(MemberProduct mp) {
		// 1) earningsRate (%)
		if (mp.getEarningsRate() != null)
			return mp.getEarningsRate();

		// 2) (valuationPl / purchaseAmount) * 100
		BigDecimal pl = nvl(mp.getValuationPl());
		BigDecimal purchase = nvl(mp.getPurchaseAmount());
		if (purchase.compareTo(BigDecimal.ZERO) > 0) {
			return pl.multiply(BigDecimal.valueOf(100)).divide(purchase, 6, RoundingMode.HALF_UP);
		}

		// 3) valuationPl(원 단위) — 직접 비교(보조척도)
		return pl; // purchase가 0인 경우엔 절대 PL이 큰 쪽을 더 좋은 성과로 간주
	}

	private static BigDecimal nvl(BigDecimal v) {
		return v == null ? BigDecimal.ZERO : v;
	}

	private static RebalanceChoice hold(String why, MemberProduct winner, BigDecimal targetIsaAmount,
		BigDecimal mpTotal, BigDecimal deposit) {
		return RebalanceChoice.builder()
			.action(RebalanceChoice.Action.HOLD)
			.mpName(winner != null ? winner.getItemName() : null)
			.mpCode(winner != null ? winner.getItemCode() : null)
			.tradeAmount(BigDecimal.ZERO)
			.targetIsaAmount(targetIsaAmount)
			.mpTotal(mpTotal)
			.deposit(deposit)
			.reason(why)
			.build();
	}
}
