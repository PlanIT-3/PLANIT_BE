package woojooin.planit.domain.rebalance.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.goal.domain.Goal;
import woojooin.planit.domain.goal.mapper.GoalMapper;
import woojooin.planit.domain.rebalance.dto.res.RebalancingInfo;
import woojooin.planit.domain.rebalance.mapper.RebalanceMapper;
import woojooin.planit.domain.rebalance.vo.Rebalance;

@Slf4j
@Service
@RequiredArgsConstructor
public class RebalanceService {

	private final GoalMapper goalMapper;
	private final RebalanceMapper rebalanceMapper;

	public List<RebalancingInfo> reqCurrentRebalancing(Long memberId) {
		List<Goal> goals = goalMapper.selectAllGoals(memberId);

		List<RebalancingInfo> infoList = new ArrayList<>();

		for (Goal goal : goals) {
			List<Rebalance> rebalanceList = rebalanceMapper.findLatestRebalanceByGoalId(goal.getGoalId());

			RebalancingInfo rebalancingInfo = new RebalancingInfo();
			rebalancingInfo.setGoalName(goal.getGoalName());
			for (Rebalance rebalance : rebalanceList) {
				if (rebalance == null) {
					continue;
				}

				rebalancingInfo.addInfo(rebalance);
			}

			infoList.add(rebalancingInfo);
		}

		return infoList;
	}
}
