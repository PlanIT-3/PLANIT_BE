package woojooin.planit.domain.account.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import woojooin.planit.domain.account.domain.BalanceData;
import woojooin.planit.domain.account.dto.res.BalanceListRes;
import woojooin.planit.domain.account.dto.res.BalanceRes;
import woojooin.planit.domain.account.mapper.AccountMapper;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountMapper accountMapper;

	public BalanceListRes getAccountBalanceForDate(Long memberId, String period) {
		List<BalanceData> balanceDataList = accountMapper.getBalanceByMemberIdAndPeriod(memberId, period);
		
		List<BalanceRes> balanceRes = balanceDataList.stream()
				.map(balance -> new BalanceRes(
						balance.getAmount(), 
						balance.getCreatedAt()))
				.collect(Collectors.toList());
		
		return new BalanceListRes(balanceRes);
	}
}
