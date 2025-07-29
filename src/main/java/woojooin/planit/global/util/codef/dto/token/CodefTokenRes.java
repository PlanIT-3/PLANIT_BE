package woojooin.planit.global.util.codef.dto.token;

public record CodefTokenRes(
	String accessToken,
	String tokenType,
	Integer expiresIn,
	String scope
) {
}

