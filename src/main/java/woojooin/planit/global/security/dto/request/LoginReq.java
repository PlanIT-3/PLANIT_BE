package woojooin.planit.global.security.dto.request;

import lombok.Data;

@Data
public class LoginReq {
    private String email;
    private String password;
}
