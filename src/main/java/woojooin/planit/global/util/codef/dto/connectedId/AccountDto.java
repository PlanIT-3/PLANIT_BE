package woojooin.planit.global.util.codef.dto.connectedId;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

	@NotNull
	private String countryCode;

	@NotNull
	private String businessType;

	@NotNull
	private String clientType;

	@NotNull
	private String organization;

	private String loginType;

	private String id;

	private String password;

	private String birthDate;
}
