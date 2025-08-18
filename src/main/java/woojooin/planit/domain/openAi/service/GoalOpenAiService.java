package woojooin.planit.domain.openAi.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.openAi.dto.req.GoalProgressReq;
import woojooin.planit.domain.openAi.dto.res.GoalProgressRes;
import woojooin.planit.domain.openAi.mapper.GoalProgressMapper;
import woojooin.planit.global.util.openAiTemp.OpenAiUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoalOpenAiService {
	private final OpenAiUtil openAiUtil;
	private final GoalProgressMapper goalProgressMapper;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public GoalProgressRes getGoalProgressAdvice(Long memberId, Long goalId) {
		try {
			// DB에서 데이터를 조회하여 GoalProgressReq 생성
			GoalProgressReq goalProgressReq = createGoalProgressReq(memberId, goalId);
			log.info("[GoalOpenAiService.getGoalProgressAdvice()] - 목표 진행 요청 데이터: {}", goalProgressReq);

			String userMessage = createUserMessage(goalProgressReq);
			String assistantMessage = createAssistantMessage();

			String response = openAiUtil.basicChat(userMessage, assistantMessage);

			return parseResponse(response);

		} catch (Exception e) {
			throw new RuntimeException("목표 진행 제언 생성 중 오류가 발생했습니다: " + e.getMessage());
		}
	}

	private GoalProgressReq createGoalProgressReq(Long memberId, Long goalId) {
		GoalProgressReq goalProgressReq = new GoalProgressReq();

		// 각 데이터를 mapper를 통해 조회
		goalProgressReq.setRiskProfile(goalProgressMapper.getRiskProfile(memberId));
		goalProgressReq.setIsaProgress(goalProgressMapper.getIsaProgress(memberId, goalId));
		goalProgressReq.setDepositProgress(goalProgressMapper.getDepositProgress(memberId, goalId));

		return goalProgressReq;
	}

	private String createUserMessage(GoalProgressReq goalProgressReq) throws Exception {
		StringBuilder message = new StringBuilder();
		message.append("목표 진행 정보:\n");
		message.append("- 투자자의 위험 성향: ").append(goalProgressReq.getRiskProfile()).append("\n");
		message.append("- 목표 달성을 위한 ISA 상품들의 목표 달성 진행률: ").append(goalProgressReq.getIsaProgress()).append("\n");
		message.append("- 목표 달성을 위한 예적금 상품들의 목표 달성 진행률: ").append(goalProgressReq.getDepositProgress()).append("\n");

		return message.toString();
	}

	private String createAssistantMessage() {
		return "위의 정보를 바탕으로 목표 진행 상황에 대한 조언을 제공해주세요. " +
			"응답 형식은 다음과 같습니다:\n" +
			"goalProgressAdvice: 구체적인 제언 텍스트" +
			"응답은 JSON 형식으로 작성해주세요. \n" +
			"각 ISA 상품과 예적금 상품의 진행률을 고려하여, " +
			"목표 달성을 위한 최적의 전략과 조언을 제시해주세요." +
			"예를 들어, " +
			"각 상품의 진행률이 목표에 비해 어떤지, " +
			"어떤 상품에 더 집중해야 하는지, " +
			"또는 추가적인 투자 전략이 필요한지 등을 포함해주세요.\n" +
			"그리고, 지금 같은 흐름으로 진행할 경우 목표 달성까지 예상되는 기간과 " +
			"목표 달성을 위한 추가적인 조언을 포함해주세요.\n" +
			"3줄 이내로 작성해주세요. 줄마다 엔터를 쳐서 가독성이 좋게 보여주세요.\n";
	}

	private GoalProgressRes parseResponse(String response) {
		try {
			// JSON 응답에서 불필요한 텍스트 제거
			String cleanResponse = extractJsonFromResponse(response);

			// JSON을 InvestReportRes 객체로 파싱
			GoalProgressRes goalProgressRes = objectMapper.readValue(cleanResponse, GoalProgressRes.class);

			// 데이터 유효성 검증
			if (goalProgressRes.getGoalProgressAdvice() == null ||
				goalProgressRes.getGoalProgressAdvice().isEmpty()) {
				throw new RuntimeException("목표 진행률 제언 데이터가 누락되었습니다.");
			}

			return goalProgressRes;

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
