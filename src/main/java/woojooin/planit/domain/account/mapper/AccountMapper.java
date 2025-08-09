package woojooin.planit.domain.account.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.account.domain.BalanceData;

@Mapper
public interface AccountMapper {

	List<BalanceData> getBalanceByMemberIdAndPeriod(@Param("memberId") Long memberId, @Param("period") String period);
	
	BigDecimal getTotalBalanceByMemberId(@Param("memberId") Long memberId);
}
