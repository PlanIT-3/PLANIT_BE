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
	public static RebalanceChoice decide(Goal goal,
		BigDecimal deposit,
		List<MemberProduct> mpList) {

		Objects.requireNonNull(goal, "goal");
		Objects.requireNonNull(deposit, "deposit");
		Objects.requireNonNull(mpList, "mpList");
		if (mpList.isEmpty())
			throw new IllegalArgumentException("mpList는 최소 1개 이상이어야 합니다.");

		// mp 합계
		BigDecimal mpTotal = mpList.stream()
			.map(mp -> nvl(mp.getValuationAmount()))
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		// 현재 총자산 및 기준자산(목표 상한 캡)
		BigDecimal V = nvl(deposit).add(mpTotal);
		BigDecimal targetAmount = nvl(BigDecimal.valueOf(
			goal.getTargetAmount() == null ? 0 : goal.getTargetAmount()
		));
		if (targetAmount.compareTo(BigDecimal.ZERO) <= 0) {
			// targetAmount가 0 또는 미설정이면 전체 포트폴리오 기준으로 간주
			targetAmount = V;
		}
		BigDecimal base = V.min(targetAmount);

		// 목표 ISA 금액
		BigDecimal isaRatePct = BigDecimal.valueOf(
			goal.getIsaRate() == null ? 0 : goal.getIsaRate()
		).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

		BigDecimal targetIsaAmount = base.multiply(isaRatePct)
			.setScale(0, RoundingMode.HALF_UP);

		// 부족/초과 금액
		BigDecimal delta = targetIsaAmount.subtract(mpTotal); // +면 매수, -면 매도

		// 수익률 높은 MP(동점이면 평가금액 큰 쪽)를 선택
		MemberProduct winner = pickTopPerformer(mpList);

		// HOLD 조건: delta=0
		if (delta.compareTo(BigDecimal.ZERO) == 0) {
			return hold("목표 비중과 동일", winner, targetIsaAmount, mpTotal, deposit);
		}

		if (delta.signum() > 0) {
			// 매수: deposit 한도 내에서, 승자 1개만
			BigDecimal buy = delta.min(nvl(deposit)).max(BigDecimal.ZERO)
				.setScale(0, RoundingMode.HALF_UP);
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
			// 매도: 승자 1개만, 그 평가금액 한도 내
			BigDecimal winnerVal = nvl(winner.getValuationAmount());
			BigDecimal sellNeeded = delta.abs();
			BigDecimal sell = sellNeeded.min(winnerVal)
				.setScale(0, RoundingMode.HALF_UP);
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

	/** 수익률 최고(동률이면 valuationAmount 큰 것) 선택 */
	private static MemberProduct pickTopPerformer(List<MemberProduct> list) {
		return list.stream()
			.filter(Objects::nonNull)
			.max(Comparator
				.comparing((MemberProduct mp) -> nvl(rate(mp)))             // earningsRate 우선
				.thenComparing(mp -> nvl(mp.getValuationAmount()))          // 동률이면 평가금액 큰 쪽
			)
			.orElseThrow(() -> new IllegalArgumentException("유효한 MP가 없습니다."));
	}

	/** earningsRate 안전 추출(BigDecimal 가정, 필요 시 변환 로직 조정) */
	private static BigDecimal rate(MemberProduct mp) {
		BigDecimal er = null;
		try {
			er = (BigDecimal)mp.getEarningsRate();
		} catch (ClassCastException ignore) {
			// 만약 Double/Float라면 아래처럼 변환:
			// Object r = mp.getEarningsRate();
			// if (r instanceof Number) er = BigDecimal.valueOf(((Number) r).doubleValue());
		}
		return nvl(er);
	}

	private static BigDecimal nvl(BigDecimal v) {
		return v == null ? BigDecimal.ZERO : v;
	}

	/** 기존 hold 빌더 재사용 */
	private static RebalanceChoice hold(String reason,
		MemberProduct winner,
		BigDecimal targetIsaAmount,
		BigDecimal mpTotal,
		BigDecimal deposit) {
		return RebalanceChoice.builder()
			.action(RebalanceChoice.Action.HOLD)
			.mpName(winner != null ? winner.getItemName() : null)
			.mpCode(winner != null ? winner.getItemCode() : null)
			.tradeAmount(BigDecimal.ZERO)
			.targetIsaAmount(targetIsaAmount)
			.mpTotal(mpTotal)
			.deposit(deposit)
			.reason(reason)
			.build();
	}

}
