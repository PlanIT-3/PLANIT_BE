package woojooin.planit.domain.openAi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import woojooin.planit.domain.openAi.dto.req.DefaultInvestTypeReq;
import woojooin.planit.domain.openAi.dto.req.InvestTypeReq;
import woojooin.planit.domain.openAi.dto.res.InvestTypeRes;
import woojooin.planit.domain.openAi.mapper.InvestTypeMapper;
import woojooin.planit.global.util.openAi.OpenAiUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvestTypeOpenAiService {
    private final OpenAiUtil openAiUtil;
    private final InvestTypeMapper investTypeMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public InvestTypeRes getInvestTypeAdvice(Long memberId) {
        try {
            InvestTypeReq investTypeReq = investTypeMapper.getInvestTypeByMemberId(memberId);
            DefaultInvestTypeReq defaultInvestTypeReq = getDefaultInvestTypeByInvestType(investTypeReq.getInvestType());
            log.info("[InvestTypeOpenAiService.getInvestTypeAdvice()] - 투자 유형 요청 데이터: {}", investTypeReq);

            String userMessage = createUserMessage(investTypeReq, defaultInvestTypeReq);
            String assistantMessage = createAssistantMessage();

            String response = openAiUtil.basicChat(userMessage, assistantMessage);

            return parseResponse(response);

        } catch (Exception e) {
            throw new RuntimeException("투자 유형 제언 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public DefaultInvestTypeReq getDefaultInvestTypeByInvestType(String investType) {
        switch (investType) {
            case "CONSERVATIVE":
                return DefaultInvestTypeReq.conservative();
            case "STABLE":
                return DefaultInvestTypeReq.stable();
            case "NEUTRAL":
                return DefaultInvestTypeReq.neutral();
            case "GROWTH":
                return DefaultInvestTypeReq.growth();
            case "AGGRESSIVE":
                return DefaultInvestTypeReq.aggressive();
        }
        throw new IllegalArgumentException("알 수 없는 투자 유형: " + investType);
    }

    private String createUserMessage(InvestTypeReq investTypeReq, DefaultInvestTypeReq defaultInvestTypeReq) {
        StringBuilder message = new StringBuilder();
        message.append("유저가 선택한 투자 유형과 실제 투자 행동에 대한 제언을 생성해주세요. 유저가 선택한 투자 유형 데이터: ");
        message.append(investTypeReq);
        message.append(", 실제 투자 행동 데이터: ");
        message.append(defaultInvestTypeReq);
        return message.toString();
    }

    private String createAssistantMessage() {
        return "유저가 선택한 투자 유형과 실제 투자 행동을 비교하여 앞으로의 투자에 대한 제언을 작성해주세요. " +
                "제언은 유저가 선택한 투자 유형에 맞춰서 작성되어야 합니다. " +
                "제언은 3가지로 나누어 작성해주세요. 각 제언은 1문장으로 짧게 작성해주세요." +
                "제언은 다음과 같은 형식으로 작성해주세요: " +
                "investTypeAdvice1: [제언 내용 String], " +
                "investTypeAdvice2: [제언 내용 String], " +
                "investTypeAdvice3: [제언 내용 String]" +
                "JSON 형식으로만 응답해주세요.";
    }

    private InvestTypeRes parseResponse(String response) {
        try {
            // JSON 응답에서 불필요한 텍스트 제거
            String cleanResponse = extractJsonFromResponse(response);

            // JSON을 InvestTypeRes 객체로 파싱
            InvestTypeRes investTypeRes = objectMapper.readValue(cleanResponse, InvestTypeRes.class);

            // 데이터 유효성 검증
            if (investTypeRes == null) {
                throw new RuntimeException("투자 유형 제언 데이터가 null입니다.");
            }

            if (investTypeRes.getInvestTypeAdvice1().trim().isEmpty() ||
                    investTypeRes.getInvestTypeAdvice2().trim().isEmpty() || investTypeRes.getInvestTypeAdvice3().trim().isEmpty()) {
                throw new RuntimeException("투자 유형 제언 데이터가 누락되었습니다.");
            }

            return investTypeRes;

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


