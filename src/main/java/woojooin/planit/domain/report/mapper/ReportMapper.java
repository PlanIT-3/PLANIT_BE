package woojooin.planit.domain.report.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.report.domain.dto.IsaCumulativeTaxSavingDTO;

import woojooin.planit.domain.report.domain.dto.IsaTaxSavingHistoryDto;

import woojooin.planit.domain.report.domain.dto.ReturnRateDto;
import woojooin.planit.domain.report.domain.enums.ReturnType;
import woojooin.planit.domain.report.api.domain.res.*;
import woojooin.planit.domain.report.domain.vo.DepositTaxSavingHistory;
import woojooin.planit.domain.report.domain.vo.IsaTaxSavingHistory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReportMapper {
    List<ReturnRateDto> findReturnRateByType(
            @Param("memberId") Long memberId,
            @Param("returnType") ReturnType returnType,
            @Param("startDate")LocalDate startDate
    );

    List<DailyRowRes> findDailyTotalInvest(
            @Param("memberId") Long memberId
    );

    List<WeeklyRowRes> findWeeklyTotalInvest(
            @Param("memberId") Long memberId
    );

    List<MonthlyRowRes> findMonthlyTotalInvest(
            @Param("memberId") Long memberId
    );

    String getIsaType(@Param("memberId")Long memberId);

    List<IsaCumulativeTaxSavingDTO> getCumulativeTaxSavingByMemberId(Long memberId);

    BigDecimal getPrincipal(Long memberId);
    IsaTaxSavingHistoryDto getLatestIsaTaxSavingHistory(Long memberId);

    Long getLatestIsaProfitByMemberId(@Param("memberId")Long memberId);
    Long getLatestGeneralTaxByMemberId(@Param("memberId")Long memberId);
    Long getLatestTaxSavedByMemberId(@Param("memberId")Long memberId);

    IsaTaxSavingHistory selectIsaTaxSavingHistory(@Param("isaTaxSavingHistoryId")Long isaTaxSavingHistoryId);

    DepositTaxSavingHistory selectDepositTaxSavingHistory(@Param("depositTaxSavingHistoryId")Long depositTaxSavingHistoryId);

    List<IsaTaxSavingHistory>  selectIsaTaxSavingHistoriesByMemberId(@Param("memberId")Long memberId);

    List<DepositTaxSavingHistory> selectDepositTaxSavingHistoriesByMemberId(@Param("memberId")Long memberId);

    List<DepositTaxSavingHistory> selectDepositTaxSavingHystoryByAccountId(@Param("accountId")Long accountId);
}
