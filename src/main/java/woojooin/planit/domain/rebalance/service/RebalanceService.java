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

		log.info("getRebalanceInfo 3");
		for (Goal goal : goals) {
			log.info("getRebalanceInfo goal={} 3-1", goal.getGoalId());
			List<Rebalance> rebalanceList = rebalanceMapper.findLatestRebalanceByGoalId(goal.getGoalId());
			log.info("getRebalanceInfo goal={} 3-2", goal.getGoalId());

			RebalancingInfo rebalancingInfo = new RebalancingInfo();
			rebalancingInfo.setGoalName(goal.getGoalName());
			for (Rebalance rebalance : rebalanceList) {
				log.info("getRebalanceInfo rebalanceId={} 3-3", rebalance.getRebalanceId());
				if (rebalance == null) {
					continue;
				}
				log.info("getRebalanceInfo rebalance={} 3-4", rebalance);
				rebalancingInfo.addInfo(rebalance);
			}

			infoList.add(rebalancingInfo);
		}
		log.info("getRebalanceInfo 4");

		return infoList;
	}
}
