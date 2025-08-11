package woojooin.planit.domain.openAi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import woojooin.planit.domain.openAi.dto.req.InvestReportReq;
import woojooin.planit.domain.openAi.dto.res.InvestReportRes;
import woojooin.planit.domain.openAi.mapper.InvestReportMapper;
import woojooin.planit.global.util.openAi.OpenAiUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvestOpenAiService {
    
    private final OpenAiUtil openAiUtil;
    private final InvestReportMapper investReportMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public InvestReportRes getInvestmentAdvice(Long memberId) {

        try {
            // DB에서 데이터를 조회하여 InvestReportReq 생성
            InvestReportReq investReportReq = createInvestReportReq(memberId);
            log.info("[OpenAiService.getInvestmentAdvice()] - 투자 요청 데이터: {}", investReportReq);
            
            String userMessage = createUserMessage(investReportReq);
            String assistantMessage = createAssistantMessage();
            
            String response = openAiUtil.basicChat(userMessage, assistantMessage);
            log.info("[OpenAiService.getInvestmentAdvice()] - OpenAI 응답: {}", response);
            
            return parseResponse(response);
            
        } catch (Exception e) {
            throw new RuntimeException("투자 제언 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 멤버 ID로 DB에서 데이터를 조회하여 InvestReportReq 생성
     */
    private InvestReportReq createInvestReportReq(Long memberId) {
        InvestReportReq investReportReq = new InvestReportReq();
        
        // 각 데이터를 mapper를 통해 조회
        investReportReq.setMonthlyInvestmentAmount(investReportMapper.getMonthlyInvestmentAmount(memberId));
        investReportReq.setMonthlyEvaluationAmount(investReportMapper.getMonthlyEvaluationAmount(memberId));
        investReportReq.setEtfReturnRate(investReportMapper.getEtfReturnRate(memberId));
        investReportReq.setRiskProfile(investReportMapper.getRiskProfile(memberId));
        
        return investReportReq;
    }
    
    private String createUserMessage(InvestReportReq investReportReq) throws JsonProcessingException {
        StringBuilder message = new StringBuilder();
        message.append("투자자 정보:\n");
        message.append("- 월별 투자 금액: ").append(investReportReq.getMonthlyInvestmentAmount()).append("\n");
        message.append("- 월별 평가 금액: ").append(investReportReq.getMonthlyEvaluationAmount()).append("\n");
        message.append("- ETF 수익률: ").append(investReportReq.getEtfReturnRate()).append("\n");
        message.append("- 위험성향: ").append(investReportReq.getRiskProfile()).append("\n");
        
        message.append("위 정보를 바탕으로 다음 형식으로 투자 제언을 작성해주세요:\n");
        message.append("{\n");
        message.append("  \"recommendedInvestmentAmount\": [6개월의 월별 각각의 권장 투자 금액을 모두 다르게 해서 숫자 배열로],\n");
        message.append("  \"investmentAdvice\": \"구체적인 투자 제언 텍스트\"\n");
        message.append("}\n\n");
        message.append("JSON 형식으로만 응답해주세요. 제언 텍스트는 3줄 이내로 작성해주세요.\n");
        
        return message.toString();
    }
    
    private String createAssistantMessage() {
        return "투자자의 위험성향, 과거 투자 성과, 가용 자금을 종합적으로 분석하여 " +
               "개인화된 투자 제언과 월별 권장 투자 금액을 JSON 형식으로 제공해줘. " +
               "안전성과 수익성의 균형을 고려한 실용적인 조언을 주는데 3줄 이내로 해줘.";
    }
    
    private InvestReportRes parseResponse(String response) {
        try {
            // JSON 응답에서 불필요한 텍스트 제거
            String cleanResponse = extractJsonFromResponse(response);

            // JSON을 InvestReportRes 객체로 파싱
            InvestReportRes investReportRes = objectMapper.readValue(cleanResponse, InvestReportRes.class);

            // 데이터 유효성 검증
            if (investReportRes.getRecommendedInvestmentAmount() == null ||
                investReportRes.getRecommendedInvestmentAmount().isEmpty()) {
                throw new RuntimeException("권장 투자 금액 데이터가 누락되었습니다.");
            }

            if (investReportRes.getInvestmentAdvice() == null ||
                investReportRes.getInvestmentAdvice().trim().isEmpty()) {
                throw new RuntimeException("투자 제언 데이터가 누락되었습니다.");
            }

            return investReportRes;

        } catch (Exception e) {
            throw new RuntimeException("응답 파싱 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    private String extractJsonFromResponse(String response) {
        // JSON 시작과 끝을 찾아서 추출
        int startIndex = response.indexOf("{");
        int endIndex = response.lastIndexOf("}");
        
        if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex) {
            throw new RuntimeException("올바른 JSON 형식의 응답을 찾을 수 없습니다.");
        }
        
        return response.substring(startIndex, endIndex + 1);
    }
}
