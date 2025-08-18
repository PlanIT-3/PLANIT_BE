package woojooin.planit.global.util.openAiTemp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OpenAiTestController {

	private final OpenAiUtil openAiUtil;

	@GetMapping("/test/openai/basic")
	public String test() {
		//return openAiUtil.basicChat();
		return "";
	}
}
