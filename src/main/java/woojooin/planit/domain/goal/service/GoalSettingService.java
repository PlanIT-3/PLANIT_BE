package woojooin.planit.domain.goal.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import woojooin.planit.domain.goal.domain.Bank;
import woojooin.planit.domain.goal.domain.Goal;
import woojooin.planit.domain.goal.domain.GoalProgress;
import woojooin.planit.domain.goal.dto.GoalAccountRateResponse;
import woojooin.planit.domain.goal.dto.GoalDetailResponseDto;
import woojooin.planit.domain.goal.dto.GoalRequestDto;
import woojooin.planit.domain.goal.dto.GoalProgressGraphDTO;
import woojooin.planit.domain.goal.mapper.GoalMapper;
import woojooin.planit.domain.goal.isa.dto.res.IsaAccountProductRes;
import woojooin.planit.domain.goal.deposit.dto.res.DepositAccountRes;
import woojooin.planit.global.exception.BusinessException;
import woojooin.planit.global.response.ResponseCode;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalSettingService {
	private final GoalMapper goalMapper;

	@Transactional
	public Goal createGoal(Long memberId, GoalRequestDto requestDto) {
		Goal goal = requestDto.toEntity();
		goal.setMemberId(memberId);
		goalMapper.insertGoal(goal);
		return goal; // 생성된 goal 객체 (ID 포함) 반환
	}

	public GoalDetailResponseDto getGoalDetail(Long memberId, Long goalId) {
		// goal 테이블에서 목표 기본 정보 조회
		Goal goal = goalMapper.selectGoalById(memberId, goalId);
		if (goal == null) {
			throw new BusinessException(ResponseCode.GOAL_NOT_FOUND);
		}
		//  isa , 예적금 상품 목록 조회
		List<IsaAccountProductRes> isaItems = goalMapper.findAllocatedIsaByGoal(memberId, goalId);
		List<DepositAccountRes> depositAccounts = goalMapper.findAllocatedDepositByGoal(memberId, goalId);

		long totalIsaAmount = isaItems.stream()
			.mapToLong(item -> item.getPresentAmount()
				.multiply(item.getQuantity() != null ? BigDecimal.valueOf(item.getQuantity()) : BigDecimal.ONE)
				.longValue())
			.sum();

		long totalDepositAmount = depositAccounts.stream()
			.mapToLong(item -> item.getAllocatedAmount().longValue())
			.sum();

		long totalCurrentAmount = totalIsaAmount + totalDepositAmount;

		int goalRate = 0;
		if (goal.getTargetAmount() != null && goal.getTargetAmount() > 0) {
			goalRate = (int)Math.floor((double)totalCurrentAmount * 100 / goal.getTargetAmount());
		}
		goal.setGoalRate(goalRate);

		goal.setStartAmount(totalCurrentAmount);

		goalMapper.updateGoal(goal);
		return GoalDetailResponseDto.builder()
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
	public List<GoalDetailResponseDto> getAllGoals(Long memberId) {
		List<Goal> goals = goalMapper.selectAllGoals(memberId);
		if (goals.isEmpty()) {
			return Collections.emptyList();
		}

		if (goals.isEmpty()) {
			return Collections.emptyList();
		}
		return goals.stream().map(goal -> {
			List<IsaAccountProductRes> isaList = goalMapper.findAllocatedIsaByGoal(memberId, goal.getGoalId());
			List<DepositAccountRes> depositList = goalMapper.findAllocatedDepositByGoal(memberId, goal.getGoalId());

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
				.mapToLong(item -> item.getAllocatedAmount().longValue())
				.sum();
			long totalCurrentAmount = totalIsaAmount + totalDepositAmount;
			int goalRate = 0;
			if (goal.getTargetAmount() != null && goal.getTargetAmount() > 0) {
				goalRate = (int)Math.floor((double)totalCurrentAmount * 100 / goal.getTargetAmount());
			}
			return GoalDetailResponseDto.builder()
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
	public Goal updateGoal(Long memberId, Long goalId, GoalRequestDto requestDto) {
		Goal updatedGoal = requestDto.toEntity();
		updatedGoal.setGoalId(goalId);
		updatedGoal.setMemberId(memberId);

		goalMapper.updateGoal(updatedGoal);
		return updatedGoal;
	}

	@Transactional
	public void deleteGoal(Long goalId, Long memberId) {
		goalMapper.deleteGoal(goalId, memberId);
	}

	public List<GoalProgressGraphDTO> getGoalProgressByGoalId(Long goalId) {
		List<GoalProgress> goalProgresses = goalMapper.selectGoalProgressByGoalId(goalId);
		if (goalProgresses == null || goalProgresses.isEmpty()) {
			throw new BusinessException(ResponseCode.ISA_PRODUCT_NOT_FOUND);
		}
		return goalProgresses.stream()
			.map(GoalProgressGraphDTO::fromEntity)
			.toList();
	}

	public List<GoalAccountRateResponse> getGoalAccountRates(Long goalId) {
		Long targetAmount = goalMapper.getTargetAmountByGoalId(goalId);

		if (targetAmount == null) {
			throw new IllegalArgumentException("해당 goalId의 목표 금액이 존재하지 않습니다: " + goalId);
		}

		List<Map<String, Object>> rows = goalMapper.getGoalAccountRates(goalId);

		return rows.stream()
			.map(row -> {
				String bankCode = (String)row.get("bankCode");
				String bankName = Bank.getNameByCode(bankCode);

				long accountBalance = ((Number)row.get("accountBalance")).longValue();
				int accountAllocatedRate = ((Number)row.get("allocatedRate")).intValue();

				double progress = (accountBalance * (accountAllocatedRate / 100.0)) / targetAmount * 100;

				return new GoalAccountRateResponse(bankName, progress);
			})
			.collect(Collectors.toList());
	}

}
