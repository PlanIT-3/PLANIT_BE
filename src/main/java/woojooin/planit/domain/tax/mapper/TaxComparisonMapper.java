package woojooin.planit.domain.tax.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import woojooin.planit.domain.tax.api.dto.res.TaxComparisonRes;

import java.util.List;

@Mapper
public interface TaxComparisonMapper {
    
    /**
     * 최근 N분기 시계열 세금 비교 데이터 조회
     */
    List<TaxComparisonRes.QuarterlyTaxData> getTaxTrendByQuarters(
        @Param("memberId") Long memberId,
        @Param("quarters") Integer quarters
    );
}