package woojooin.planit.domain.tax.api.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaxComparisonRes {
    
    private List<QuarterlyTaxData> quarterlyData;
    private TaxSummary summary;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuarterlyTaxData {
        private String quarter;                 // 분기 (예: "2024-Q1")
        
        // ISA 관련
        private BigDecimal isaProfit;           // ISA 수익
        private BigDecimal isaGeneralTax;       // 일반 투자 시 세금
        private BigDecimal isaTaxSaved;         // ISA 절세 금액
        
        // 예적금 관련  
        private BigDecimal depositInterest;     // 예적금 이자수익
        private BigDecimal depositTax;          // 예적금 세금 (소득세+지방소득세)
        private BigDecimal depositNetIncome;    // 예적금 세후수익
        
        // 비교
        private BigDecimal totalTaxDifference;  // 전체 세금 차이
        private BigDecimal taxSavingRate;       // 절세율 (%)
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaxSummary {
        private BigDecimal totalIsaTaxSaved;    // 총 ISA 절세액
        private BigDecimal totalDepositTax;     // 총 예적금 세금
        private BigDecimal totalTaxSaving;      // 총 절세액
        private BigDecimal averageTaxSavingRate; // 평균 절세율
        private int dataCount;                  // 데이터 개수 (분기 수)
    }
}