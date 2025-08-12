package woojooin.planit.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import woojooin.planit.domain.report.domain.IsaTaxSavingStatusDTO;
import woojooin.planit.domain.report.domain.ReturnRateDto;
import woojooin.planit.domain.report.domain.ReturnType;
import woojooin.planit.domain.report.domain.res.*;
import woojooin.planit.domain.report.mapper.ReportMapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportMapper reportMapper;

    @Transactional(readOnly = true)
    public List<ReturnRateDto>getReturnRateByType(Long memberId, ReturnType returnType, LocalDate startDate){
        return reportMapper.findReturnRateByType(memberId, returnType, startDate);
    }

    public DailyTotalInvestRes getDailyTotalInvest(Long memberId) {
        List<DailyRowRes> rows = reportMapper.findDailyTotalInvest(memberId);

        return new DailyTotalInvestRes(
                rows.stream().map(DailyRowRes::getDate).toList(),
                rows.stream().map(DailyRowRes::getDailyInvestTotal).toList(),
                rows.stream().map(DailyRowRes::getDailyValuationTotal).toList()
        );
    }

    public WeeklyTotalInvestRes getWeeklyTotalInvestment(Long memberId) {
        List<WeeklyRowRes> rows = reportMapper.findWeeklyTotalInvest(memberId);

        return new WeeklyTotalInvestRes(
                rows.stream().map(WeeklyRowRes::getDate).toList(),
                rows.stream().map(WeeklyRowRes::getWeeklyInvestTotal).toList(),
                rows.stream().map(WeeklyRowRes::getWeeklyValuationTotal).toList()
        );
    }

    public MonthlyTotalInvestRes getMonthlyTotalInvestment(Long memberId) {
        List<MonthlyRowRes> rows = reportMapper.findMonthlyTotalInvest(memberId);

        return new MonthlyTotalInvestRes(
                rows.stream().map(MonthlyRowRes::getDate).toList(),
                rows.stream().map(MonthlyRowRes::getMonthlyInvestTotal).toList(),
                rows.stream().map(MonthlyRowRes::getMonthlyValuationTotal).toList()
        );
    }

    public IsaTaxSavingStatusDTO getTaxSavingStatus(Long memberId) {
        String isaType = reportMapper.getIsaType(memberId);
        int maxTaxSavingLimit = "RURAL".equals(isaType) ? 4000000 : 2000000;
        int currentTaxSaving = reportMapper.getCurrentTaxSaving(memberId);

        IsaTaxSavingStatusDTO dto = new IsaTaxSavingStatusDTO();
        dto.setMaxTaxSavingLimit(maxTaxSavingLimit);
        dto.setCurrentTaxSaving(currentTaxSaving);
        dto.calculateFields();

        return dto;
    }

}
