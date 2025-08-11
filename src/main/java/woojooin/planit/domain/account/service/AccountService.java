package woojooin.planit.domain.account.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import woojooin.planit.domain.account.domain.BalanceData;
import woojooin.planit.domain.account.dto.res.BalanceListRes;
import woojooin.planit.domain.account.dto.res.BalanceRes;
import woojooin.planit.domain.account.mapper.AccountMapper;
import woojooin.planit.domain.goal.domain.Goal;
import woojooin.planit.domain.goal.dto.res.GoalRatioListRes;
import woojooin.planit.domain.goal.dto.res.GoalRatioRes;
import woojooin.planit.domain.goal.mapper.GoalMapper;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountMapper accountMapper;
	private final GoalMapper goalMapper;

	public BalanceListRes getAccountBalanceForDate(Long memberId, String period) {
		String startDate = calculateStartDate(period);
		List<BalanceData> balanceDataList = accountMapper.getBalanceByMemberIdAndPeriod(memberId, period, startDate);
		
		List<BalanceRes> balanceRes = balanceDataList.stream()
				.map(balance -> new BalanceRes(
						balance.getAmount(), 
						balance.getCreatedAt()))
				.collect(Collectors.toList());
		
		return new BalanceListRes(balanceRes);
	}
	
	private String calculateStartDate(String period) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDateTime;
		
		switch (period) {
			case "day":
				startDateTime = now.minusDays(7);
				break;
			case "week":
				startDateTime = now.minusWeeks(6);
				break;
			case "month":
				startDateTime = now.minusMonths(6);
				break;
			default:
				startDateTime = now.minusDays(7);
				break;
		}
		
		return startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public GoalRatioListRes getGoalRatioBasedOnAccount(Long memberId) {
		BigDecimal totalBalance = accountMapper.getTotalBalanceByMemberId(memberId);
		List<Goal> goals = goalMapper.selectAllGoals(memberId);
		
		List<GoalRatioRes> goalRatios = goals.stream()
				.map(goal -> {
					BigDecimal ratio = BigDecimal.ZERO;
					if (totalBalance.compareTo(BigDecimal.ZERO) > 0) {
						ratio = new BigDecimal(goal.getTargetAmount())
								.divide(totalBalance, 4, RoundingMode.HALF_UP)
								.multiply(new BigDecimal("100"));
					}
					return new GoalRatioRes(goal.getGoalName(), goal.getTargetAmount(), ratio);
				})
				.collect(Collectors.toList());
		
		return new GoalRatioListRes(totalBalance, goalRatios);
	}
}
