package woojooin.planit.global.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import woojooin.planit.global.util.codef.dto.connectedId.create.ConntectedIdCreateRes;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AccountConnectionRes {

	private ConntectedIdCreateRes conntectedIdCreateRes;
	private String accessToken;
	private String refreshToken;
}
