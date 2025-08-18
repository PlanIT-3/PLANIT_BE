package woojooin.planit.domain.account.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.account.domain.Account;
import woojooin.planit.domain.account.domain.BalanceData;
import woojooin.planit.domain.account.dto.res.AccountBankRes;

@Mapper
public interface AccountMapper {

	List<BalanceData> getBalanceByMemberIdAndPeriod(@Param("memberId") Long memberId, @Param("period") String period,
		@Param("startDate") String startDate);

	BigDecimal getTotalBalanceByMemberId(@Param("memberId") Long memberId);

	void insertAccount(Account account);

	List<AccountBankRes> selectAccountsByMemberId(@Param("memberId") Long memberId);

	Account findAccountById(@Param("accountId") Long accountId);
}
