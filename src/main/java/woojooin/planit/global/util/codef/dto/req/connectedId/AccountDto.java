package woojooin.planit.global.util.codef.dto.req.connectedId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AccountDto {
	private String countryCode;

	private String businessType;

	private String clientType;

	private String organization;

	private String loginType;

	private String id;

	private String password;

	private String birthDate;
}
