package woojooin.planit.domain.tax.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import woojooin.planit.domain.tax.api.dto.res.TaxComparisonRes;
import woojooin.planit.domain.tax.mapper.TaxComparisonMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaxComparisonService {
    
    private final TaxComparisonMapper taxComparisonMapper;
    
    /**
     * 분기별 시계열 세금 비교 데이터 조회
     */
    public TaxComparisonRes getTaxTrend(Long memberId, Integer quarters) {
        log.info("Getting tax trend for member: {}, quarters: {}", memberId, quarters);
        
        // 시계열 분기별 데이터 조회 (최근 N분기)
        List<TaxComparisonRes.QuarterlyTaxData> quarterlyData = 
            taxComparisonMapper.getTaxTrendByQuarters(memberId, quarters);
        
        // 요약 데이터 계산
        TaxComparisonRes.TaxSummary summary = calculateSummary(quarterlyData);
        
        return TaxComparisonRes.builder()
            .quarterlyData(quarterlyData)
            .summary(summary)
            .build();
    }
    
    /**
     * 요약 데이터 계산
     */
    private TaxComparisonRes.TaxSummary calculateSummary(List<TaxComparisonRes.QuarterlyTaxData> quarterlyData) {
        if (quarterlyData.isEmpty()) {
            return TaxComparisonRes.TaxSummary.builder()
                .totalIsaTaxSaved(BigDecimal.ZERO)
                .totalDepositTax(BigDecimal.ZERO)
                .totalTaxSaving(BigDecimal.ZERO)
                .averageTaxSavingRate(BigDecimal.ZERO)
                .dataCount(0)
                .build();
        }
        
        BigDecimal totalIsaTaxSaved = quarterlyData.stream()
            .map(data -> data.getIsaTaxSaved() != null ? data.getIsaTaxSaved() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalDepositTax = quarterlyData.stream()
            .map(data -> data.getDepositTax() != null ? data.getDepositTax() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal totalTaxSaving = quarterlyData.stream()
            .map(data -> data.getTotalTaxDifference() != null ? data.getTotalTaxDifference() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        BigDecimal averageTaxSavingRate = quarterlyData.stream()
            .map(data -> data.getTaxSavingRate() != null ? data.getTaxSavingRate() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(quarterlyData.size()), 2, RoundingMode.HALF_UP);
        
        return TaxComparisonRes.TaxSummary.builder()
            .totalIsaTaxSaved(totalIsaTaxSaved)
            .totalDepositTax(totalDepositTax)
            .totalTaxSaving(totalTaxSaving)
            .averageTaxSavingRate(averageTaxSavingRate)
            .dataCount(quarterlyData.size())
            .build();
    }
}