package woojooin.planit.domain.goal.service;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import woojooin.planit.domain.goal.api.dto.res.*;
import woojooin.planit.domain.goal.domain.enums.Bank;
import woojooin.planit.domain.goal.domain.vo.Goal;
import woojooin.planit.domain.goal.domain.vo.GoalProgress;
import woojooin.planit.domain.goal.domain.dto.DepositAccountDto;
import woojooin.planit.domain.goal.api.dto.req.GoalReq;
import woojooin.planit.domain.goal.domain.dto.GoalProgressGraphDto;
import woojooin.planit.domain.goal.domain.dto.IsaProductDto;
import woojooin.planit.domain.goal.mapper.GoalMapper;
import woojooin.planit.domain.goal.api.dto.res.GoalDepositAmountRes;
import woojooin.planit.global.exception.BusinessException;
import woojooin.planit.global.response.ResponseCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalSettingService {
	private static final Logger log = LoggerFactory.getLogger(GoalSettingService.class);
	private final GoalMapper goalMapper;

	@Transactional
	public Goal createGoal(Long memberId, GoalReq requestDto) {
		Goal goal = requestDto.toEntity();
		goal.setMemberId(memberId);
		goalMapper.insertGoal(goal);
		return goal; // 생성된 goal 객체 (ID 포함) 반환
	}

	public GoalDetailRes getGoalDetail(Long memberId, Long goalId) {
		// goal 테이블에서 목표 기본 정보 조회
		Goal goal = goalMapper.selectGoalById(memberId, goalId);
		if (goal == null) {
			throw new BusinessException(ResponseCode.GOAL_NOT_FOUND);
		}
		//  isa , 예적금 상품 목록 조회
		List<IsaAccountProductRes> isaItems = goalMapper.findAllocatedIsaByGoal(memberId, goalId);
		List<GoalDepositAmountRes> depositAccounts = goalMapper.findAllocatedDepositByGoal(memberId, goalId);

		long totalIsaAmount = isaItems.stream()
			.mapToLong(item -> item.getPresentAmount()
				.multiply(item.getQuantity() != null ? BigDecimal.valueOf(item.getQuantity()) : BigDecimal.ONE)
				.longValue())
			.sum();

		long totalDepositAmount = depositAccounts.stream()
			.mapToLong(item -> item.getMyAmount().longValue())
			.sum();

		long totalCurrentAmount = totalIsaAmount + totalDepositAmount;
		log.info("Total ISA Amount: {}, Total Deposit Amount: {}, Total Current Amount: {}",
			totalIsaAmount, totalDepositAmount, totalCurrentAmount);

		int goalRate = 0;
		if (goal.getTargetAmount() != null && goal.getTargetAmount() > 0) {
			goalRate = (int)Math.floor((double)totalCurrentAmount * 100 / goal.getTargetAmount());
		}
		goal.setGoalRate(goalRate);

		goal.setStartAmount(totalCurrentAmount);

		goalMapper.updateGoal(goal);
		return GoalDetailRes.builder()
			.goalId(goal.getGoalId())
			.goalName(goal.getGoalName())
			.targetAmount(goal.getTargetAmount())
			.totalAmount(totalCurrentAmount)
			.goalRate(goalRate)
			.startDate(goal.getStartDate())
			.endDate(goal.getEndDate())
			.depositRate(goal.getDepositRate())
			.isaRate(goal.getIsaRate())
			.isaProducts(isaItems)
			.depositAccounts(depositAccounts)
			.build();
	}

	/* 리스트 조회 */
	@Transactional(readOnly = true)
	public List<GoalDetailRes> getAllGoals(Long memberId) {
		List<Goal> goals = goalMapper.selectAllGoals(memberId);
		if (goals.isEmpty()) {
			return Collections.emptyList();
		}

		if (goals.isEmpty()) {
			return Collections.emptyList();
		}
		return goals.stream().map(goal -> {
			List<IsaAccountProductRes> isaList = goalMapper.findAllocatedIsaByGoal(memberId, goal.getGoalId());
			List<GoalDepositAmountRes> depositList = goalMapper.findAllocatedDepositByGoal(memberId, goal.getGoalId());

			long totalIsaAmount =
				isaList.stream()
					.map(i -> {
						BigDecimal price = i.getPresentAmount(); // 단가
						BigDecimal qty = i.getQuantity() != null
							? BigDecimal.valueOf(i.getQuantity())  // Integer → BigDecimal
							: BigDecimal.ONE;
						return price.multiply(qty);                    // 총액
					})
					.reduce(BigDecimal.ZERO, BigDecimal::add)
					.longValue();

			long totalDepositAmount = depositList.stream()
				.mapToLong(item -> item.getMyAmount().longValue())
				.sum();

			long totalCurrentAmount = totalIsaAmount + totalDepositAmount;
			int goalRate = 0;

			if (goal.getTargetAmount() != null && goal.getTargetAmount() > 0) {
				goalRate = (int)Math.floor((double)totalCurrentAmount * 100 / goal.getTargetAmount());
			}
			return GoalDetailRes.builder()
				.goalId(goal.getGoalId())
				.goalName(goal.getGoalName())
				.targetAmount(goal.getTargetAmount())
				.totalAmount(totalCurrentAmount)
				.goalRate(goalRate)
				.startDate(goal.getStartDate())
				.endDate(goal.getEndDate())
				.depositRate(goal.getDepositRate())
				.isaRate(goal.getIsaRate())
				.isaProducts(isaList)
				.depositAccounts(depositList)
				.build();

		}).collect(Collectors.toList());
	}

	@Transactional
	public Goal updateGoal(Long memberId, Long goalId, GoalReq requestDto) {
		Goal updatedGoal = requestDto.toEntity();
		updatedGoal.setGoalId(goalId);
		updatedGoal.setMemberId(memberId);

		goalMapper.updateGoal(updatedGoal);
		return updatedGoal;
	}

//	@Transactional
//	public void deleteGoal(Long goalId, Long memberId) {
//		goalMapper.softDeleteActionsByGoalId(goalId,memberId);
//		goalMapper.deleteGoal(goalId, memberId);
//
//	}

	public List<GoalProgressGraphDto> getGoalProgressByGoalId(Long goalId) {
		List<GoalProgress> goalProgresses = goalMapper.selectGoalProgressByGoalId(goalId);
		if (goalProgresses == null || goalProgresses.isEmpty()) {
			throw new BusinessException(ResponseCode.ISA_PRODUCT_NOT_FOUND);
		}
		return goalProgresses.stream()
			.map(GoalProgressGraphDto::fromEntity)
			.toList();
	}

	public List<GoalAccountRateRes> getGoalAccountRates(Long goalId) {
		Long targetAmount = goalMapper.selectGoalTargetAmount(goalId);
		if (targetAmount == null || targetAmount <= 0) {
			throw new IllegalArgumentException("목표 금액이 없습니다: " + goalId);
		}

		List<GoalAccountRateRes> result = new ArrayList<>();

		// 1. 예적금 계좌별 비율
		List<DepositAccountDto> depositAccounts = goalMapper.selectDepositAccountsByGoalId(goalId);
		for (DepositAccountDto acc : depositAccounts) {
			BigDecimal balance = acc.getAccountBalance() != null ? acc.getAccountBalance() : BigDecimal.ZERO;
			BigDecimal rate = acc.getAllocatedRate() != null ? BigDecimal.valueOf(acc.getAllocatedRate()) : BigDecimal.ZERO;

			BigDecimal progress = balance
				.multiply(rate.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP))
				.divide(BigDecimal.valueOf(targetAmount), 6, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(100))
				.setScale(2, RoundingMode.HALF_UP);

			// bankCode -> 한글 이름 변환
			String bankName = Bank.getNameByCode(acc.getBankCode());
			if (bankName == null) bankName = acc.getBankCode(); // 못 찾으면 코드 그대로

			result.add(new GoalAccountRateRes(bankName, progress.doubleValue()));
		}

		// 2. ISA 계좌 전체 금액 합산
		List<IsaProductDto> isaProducts = goalMapper.selectIsaProductsByGoalId(goalId);
		BigDecimal totalIsaAmount = isaProducts.stream()
			.map(p -> {
				BigDecimal presentAmount = p.getPresentAmount() != null ? p.getPresentAmount() : BigDecimal.ZERO;
				BigDecimal quantity = p.getQuantity() != null ? p.getQuantity() : BigDecimal.ZERO;
				return presentAmount.multiply(quantity);
			})
			.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal isaProgress = totalIsaAmount
			.divide(BigDecimal.valueOf(targetAmount), 6, RoundingMode.HALF_UP)
			.multiply(BigDecimal.valueOf(100))
			.setScale(2, RoundingMode.HALF_UP);

		result.add(new GoalAccountRateRes("ISA", isaProgress.doubleValue()));

		return result;
	}



	private String safeString(Map<String, Object> row, String key) {
		Object val = row.get(key);
		return val != null ? val.toString() : "";
	}

	private BigDecimal safeBigDecimal(Map<String, Object> row, String key) {
		Object val = row.get(key);
		if (val instanceof BigDecimal) return (BigDecimal) val;
		if (val instanceof Number) return BigDecimal.valueOf(((Number) val).doubleValue());
		return BigDecimal.ZERO;
	}


	public List<DailyGoalProgressRes> getGoalProgress(Long goalId) {
		List<GoalProgress> goalProgresses = goalMapper.selectDailyGoalProgressLast6Months(goalId);
		if (goalProgresses == null || goalProgresses.isEmpty()) {
			throw new BusinessException(ResponseCode.GOAL_NOT_FOUND);
		}
		return goalProgresses.stream()
			.map(DailyGoalProgressRes::fromEntity)
			.toList();
	}
}
