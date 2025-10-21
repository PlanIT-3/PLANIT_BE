package woojooin.planit.domain.rebalance.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.account.domain.Account;
import woojooin.planit.domain.account.mapper.AccountMapper;
import woojooin.planit.domain.goal.domain.vo.Action;
import woojooin.planit.domain.goal.domain.enums.ActionType;
import woojooin.planit.domain.goal.mapper.ActionMapper;
import woojooin.planit.domain.goal.domain.vo.Goal;
import woojooin.planit.domain.goal.mapper.GoalMapper;
import woojooin.planit.domain.member.domain.MemberProduct;
import woojooin.planit.domain.member.mapper.MemberProductMapper;
import woojooin.planit.domain.product.domain.repository.EtfDailyHistoryRepository;
import woojooin.planit.domain.rebalance.dto.res.RebalanceInvestInfoRes;
import woojooin.planit.domain.rebalance.dto.res.RebalancingInfo;
import woojooin.planit.domain.rebalance.mapper.RebalanceMapper;
import woojooin.planit.domain.rebalance.vo.Rebalance;
import woojooin.planit.global.util.calc.RebalanceCalc;
import woojooin.planit.global.util.calc.dto.RebalanceChoice;

@Slf4j
@Service
@RequiredArgsConstructor
public class RebalanceService {

	private final GoalMapper goalMapper;
	private final RebalanceMapper rebalanceMapper;
	private final EtfDailyHistoryRepository etfDailyHistoryRepository;
	private final ActionMapper actionMapper;
	private final AccountMapper accountMapper;
	private final MemberProductMapper memberProductMapper;

	public List<RebalancingInfo> reqCurrentRebalancing(Long memberId) {
		List<Goal> goals = goalMapper.selectAllGoals(memberId);

		List<RebalancingInfo> infoList = new ArrayList<>();

		for (Goal goal : goals) {

			List<Rebalance> rebalanceList = rebalanceMapper.findLatestRebalanceByGoalId(goal.getGoalId());

			RebalancingInfo rebalancingInfo = new RebalancingInfo();
			rebalancingInfo.setGoalName(goal.getGoalName());

			for (Rebalance rebalance : rebalanceList) {
				rebalancingInfo.addInfo(rebalance);
			}

			infoList.add(rebalancingInfo);
		}

		return infoList;
	}

	public List<RebalanceInvestInfoRes> getRebalanceInvestInfo(Long memberId) {
		List<RebalanceInvestInfoRes> investInfoList = rebalanceMapper.findRebalanceInvestInfoByMemberId(memberId);
		if (investInfoList == null || investInfoList.isEmpty()) {
			log.warn("No rebalance invest info found for memberId: {}", memberId);
			return new ArrayList<>();
		}
		return investInfoList;
	}

	/**
	 * 목표별 포트폴리오 분배 리밸런싱 결과
	 * @param memberId
	 * @return
	 */
	public List<RebalanceChoice> getRebalanceChoice(Long memberId) {
		List<Goal> goals = goalMapper.selectAllGoals(memberId);

		List<RebalanceChoice> choiceList = new ArrayList<>();

		for (Goal goal : goals) {
			List<Action> actionList = actionMapper.findActionsByGoalId(goal.getGoalId());

			Account account = null;
			List<MemberProduct> memberProductList = new ArrayList<>();

			for (Action action : actionList) {

				if (action.getAccountType().equals(ActionType.DEPOSIT)) {
					account = accountMapper.findAccountById(action.getAccountId());
				} else {
					memberProductList.add(memberProductMapper.findByMemberProductId(action.getMemberProductId()));
				}
			}

			BigDecimal accountBalance = account.getAccountBalance();
			int depositRate = goal.getDepositRate();

			BigDecimal rate = BigDecimal.valueOf(depositRate)
				.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

			// 실제 금액 계산
			BigDecimal depositAmount = accountBalance.multiply(rate);

			RebalanceChoice choice = RebalanceCalc.decide(goal, depositAmount, memberProductList);
			choice.setGoalName(goal.getGoalName());
			choiceList.add(choice);
		}
		return choiceList;
	}
}
