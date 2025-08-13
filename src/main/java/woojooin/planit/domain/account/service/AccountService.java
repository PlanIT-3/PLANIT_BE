package woojooin.planit.domain.account.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.account.domain.BalanceData;
import woojooin.planit.domain.account.dto.res.BalanceListRes;
import woojooin.planit.domain.account.dto.res.BalanceRes;
import woojooin.planit.domain.account.mapper.AccountMapper;
import woojooin.planit.domain.goal.domain.Goal;
import woojooin.planit.domain.goal.dto.res.GoalRatioListRes;
import woojooin.planit.domain.goal.dto.res.GoalRatioRes;
import woojooin.planit.domain.goal.mapper.GoalMapper;
import woojooin.planit.domain.member.mapper.MemberMapper;
import woojooin.planit.global.security.dto.response.AccountConnectionRes;
import woojooin.planit.global.security.jwt.JwtTokenProvider;
import woojooin.planit.global.util.codef.CodefAccountUtil;
import woojooin.planit.global.util.codef.dto.connectedId.AccountDto;
import woojooin.planit.global.util.codef.dto.connectedId.create.ConntectedIdCreateRes;
import woojooin.planit.global.util.codef.dto.token.CodefTokenRes;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

	private final MemberMapper memberMapper;
	private final JwtTokenProvider jwtTokenProvider;
	private final CodefAccountUtil codefAccountUtil;
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

	public AccountConnectionRes register(AccountDto accountDto, boolean isLast, Long memberId) {
		log.info("isLast:{}", isLast);
		String connectedId = memberMapper.findByConnectedIdString(memberId);
		List<AccountDto> singleAccount = List.of(accountDto);
		ConntectedIdCreateRes result = null;
		
		if (connectedId == null) {
			// connectedId가 없는 경우 - 첫 번째 계좌이므로 registerConnectedId 호출
			result = codefAccountUtil.registerConnectedId(singleAccount);
			// connectedId를 얻어서 member 테이블 업데이트
			String newConnectedId = result.getConnectedId();
			log.info("newConnectedId:{}", newConnectedId);
			memberMapper.updateConnectedId(memberId, newConnectedId);
			
			// connectedId가 새로 생성된 경우에서 isLast가 true일 때만 토큰 재발급
			if (isLast) {
				String newAccessToken = jwtTokenProvider.createValidatedAccessToken(memberId, "SEMI_USER");
				String newRefreshToken = jwtTokenProvider.createValidatedRefreshToken(memberId, "SEMI_USER");
				log.info("newAccessToken:{}", newAccessToken);
				log.info("newRefreshToken:{}", newRefreshToken);
				return new AccountConnectionRes(result, newAccessToken, newRefreshToken);
			} else {
				return new AccountConnectionRes(result, "", "");
			}
		} else {
			// connectedId가 존재하는 경우 - addAccount 호출 (토큰 재발급 없음)
			log.info("connectedId:{}", connectedId);
			result = codefAccountUtil.addAccount(singleAccount, connectedId);
			return new AccountConnectionRes(result, "", "");
		}
	}
}
