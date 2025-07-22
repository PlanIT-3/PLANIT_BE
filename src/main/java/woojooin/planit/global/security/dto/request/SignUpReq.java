package woojooin.planit.global.security.dto.request;

import lombok.Data;

@Data
public class SignUpReq {
	private String email;
	private String password;
	private String nickname;

}
