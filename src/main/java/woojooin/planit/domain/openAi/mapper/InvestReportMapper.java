package woojooin.planit.domain.openAi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import woojooin.planit.domain.openAi.dto.res.InvestReportRes;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface InvestReportMapper {
    
    /**
     * 멤버의 월별 투자 금액 조회 (최근 12개월)
     */
    List<BigDecimal> getMonthlyInvestmentAmount(@Param("memberId") Long memberId);
    
    /**
     * 멤버의 월별 평가 금액 조회 (최근 12개월)
     */
    List<BigDecimal> getMonthlyEvaluationAmount(@Param("memberId") Long memberId);
    
    /**
     * 멤버의 ETF 수익률 조회
     */
    List<BigDecimal> getEtfReturnRate(@Param("memberId") Long memberId);
    
    /**
     * 멤버의 위험성향 조회
     */
    String getRiskProfile(@Param("memberId") Long memberId);
    
    /**
     * 멤버의 총 가용 자금 조회
     */

    /**
     * JSON 문자열을 InvestReportRes 객체로 파싱
     */
    InvestReportRes parseJsonToInvestReportRes(@Param("jsonString") String jsonString);
}
