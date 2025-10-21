package woojooin.planit.domain.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import woojooin.planit.domain.report.domain.dto.AccountComparisonDTO;
import woojooin.planit.domain.report.domain.dto.IsaCumulativeTaxSavingDTO;
import woojooin.planit.domain.report.domain.dto.IsaTaxSavingHistoryDto;

import woojooin.planit.domain.report.domain.dto.IsaTaxSavingStatusDTO;
import woojooin.planit.domain.report.domain.dto.ReturnRateDto;
import woojooin.planit.domain.report.domain.enums.ReturnType;
import woojooin.planit.domain.report.api.domain.res.*;
import woojooin.planit.domain.report.mapper.ReportMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
        Long result = reportMapper.getLatestIsaProfitByMemberId(memberId);
        int currentTaxSaving = (result != null) ? result.intValue() : 0;
        IsaTaxSavingStatusDTO dto = new IsaTaxSavingStatusDTO();
        dto.setMaxTaxSavingLimit(maxTaxSavingLimit);
        dto.setCurrentTaxSaving(currentTaxSaving);
        dto.calculateFields();

        return dto;
    }

    public List<IsaCumulativeTaxSavingDTO> getCumulativeTaxSaving(Long memberId) {
        return reportMapper.getCumulativeTaxSavingByMemberId(memberId);
    }

    public AccountComparisonDTO getAccountComparison(Long memberId) {
        BigDecimal principalDecimal = reportMapper.getPrincipal(memberId);
        IsaTaxSavingHistoryDto taxHistory = reportMapper.getLatestIsaTaxSavingHistory(memberId);

        String isaType = reportMapper.getIsaType(memberId);

        long generalTaxLong = taxHistory != null ? taxHistory.getGeneralTax() : 0L;
        long taxSavedLong = taxHistory != null ? taxHistory.getTaxSaved() : 0L;

        BigDecimal generalTax = BigDecimal.valueOf(generalTaxLong);
        BigDecimal taxSaved = BigDecimal.valueOf(taxSavedLong);

        BigDecimal isaTax = generalTax.subtract(taxSaved);

        long isaTotalAmount = principalDecimal.subtract(isaTax).longValue();
        long generalTotalAmount = principalDecimal.subtract(generalTax).longValue();

        double taxSavingRate = generalTaxLong == 0 ? 0.0 : ((double) taxSavedLong / generalTaxLong) * 100;

        AccountComparisonDTO dto = new AccountComparisonDTO();
        dto.setPrincipal(principalDecimal.longValue());  // 만약 DTO에 long 타입이라면 longValue()로 변환
        dto.setIsaTax(isaTax.longValue());
        dto.setIsaTotalAmount(isaTotalAmount);
        dto.setGeneralTax(generalTaxLong);
        dto.setGeneralTotalAmount(generalTotalAmount);
        dto.setTaxSaved(taxSavedLong);
        dto.setTaxSavingRate(taxSavingRate);

        return dto;
    }
}
