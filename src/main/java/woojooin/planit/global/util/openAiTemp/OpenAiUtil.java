package woojooin.planit.global.util.openAiTemp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import lombok.RequiredArgsConstructor;
import woojooin.planit.global.exception.BusinessException;
import woojooin.planit.global.response.ResponseCode;

@Component
@RequiredArgsConstructor
public class OpenAiUtil {

	@Value("${ai.prompt}")
	private String keyWordPrompt;

	private final OpenAIClient openAIClient;

	public String basicChat(String userMessage, String assistantMessage) {
		ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
			.addAssistantMessage(userMessage)
			.addUserMessage(assistantMessage)
			.model(ChatModel.O3_MINI)
			.build();

		ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);

		if (chatCompletion.choices().isEmpty()) {
			throw new BusinessException(ResponseCode.BAD_REQUEST);
		}

		String message = chatCompletion.choices().get(0).message().content()
			.orElseThrow(() -> new BusinessException(ResponseCode.BAD_REQUEST));

		return message;
	}

}
