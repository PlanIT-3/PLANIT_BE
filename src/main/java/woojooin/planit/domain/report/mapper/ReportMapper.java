package woojooin.planit.domain.report.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import woojooin.planit.domain.report.domain.ReturnRateDto;
import woojooin.planit.domain.report.domain.ReturnType;
import woojooin.planit.domain.report.domain.res.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    int getCurrentTaxSaving(@Param("memberId")Long memberId);
}
