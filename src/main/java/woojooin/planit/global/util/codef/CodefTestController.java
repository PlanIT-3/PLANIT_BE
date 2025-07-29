package woojooin.planit.global.util.codef;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.global.util.codef.dto.connectedId.AccountDto;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CodefTestController {

	private final CodefAccountUtil codefAccountUtil;

	@GetMapping("/test/codef/token")
	public ResponseEntity test() {

		return ResponseEntity.ok(codefAccountUtil.getAccessToken());
	}

	@GetMapping("/test/codef/create/connected-id")
	public ResponseEntity testCreateToken() throws JsonProcessingException {

		AccountDto dto = AccountDto.builder()
			.countryCode("KR")
			.businessType("BK")
			.clientType("P")
			.organization("0020")
			.loginType("1")
			.id("smdmim")
			.password(
				"Q1MJyLi6IRKe9cZngDNjVTzkHIArf2QsDyEburAaIRJ+lJtepIbtS9Z4Ay80EId/i6ZOiZJvgvKWVl8MIagRj9tBtcOn0HXKcB6wBk+Avz/NcIp3ZOo4+nisKsfIX0UUy4x1gaR9Z0UygoOndfZkYh3tQ6lTVNzof9YaSpzLSZueFapdl+kSJQZJUTyiSnXXYb9nmTpM8xhFYFKqjIpNR+4s2XTvM6pLYdN0PsA03h0Fnr7cJ7vjJ1/VVNp3WVJeVjLhW0foh75uy7axJy/JBzKN9ld3zUepAySHDMmFilVz5t4rUSqt3ed2X9ydnEh9+gNABo0/1HK9xM9K2mRRhQ==")
			.birthDate("000125")
			.build();

		return ResponseEntity.ok(codefAccountUtil.registerConnectedId(List.of(dto)));
	}
}
