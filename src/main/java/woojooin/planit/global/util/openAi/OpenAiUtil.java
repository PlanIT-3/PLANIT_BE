package woojooin.planit.global.util.openAi;

import org.springframework.stereotype.Component;

import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OpenAiUtil {
	private final OpenAIClient openAIClient;

	/**
	 * o3-mini 모델 기반 gpt user, assistant 메세지 통신 메서드
	 * @param userMessage
	 * @param assistantMessage
	 * @return
	 */
	public String basicChat(String userMessage, String assistantMessage) {
		ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
			.addAssistantMessage(assistantMessage)
			.addUserMessage(userMessage)
			.model(ChatModel.O3_MINI)
			.build();

		ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);

		if (chatCompletion.choices().isEmpty()) {
			throw new RuntimeException("empty response");
		}

		String message = chatCompletion.choices().get(0).message().content()
			.orElseThrow(() -> new RuntimeException("no message"));

		return message;
	}
}
