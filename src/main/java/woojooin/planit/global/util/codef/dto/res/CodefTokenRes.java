package woojooin.planit.global.util.codef.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CodefTokenRes(
	@JsonProperty("access_token") String accessToken,
	@JsonProperty("token_type") String tokenType,
	@JsonProperty("expires_in") Integer expiresIn,
	@JsonProperty("scope") String scope
) {
}

