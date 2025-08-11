package woojooin.planit.global.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginRes {
    private String email;
    private String password;
    private String username;
    private String accessToken;
    private String refreshToken;

    // accessToken만 받는 생성자 추가
    public LoginRes(String accessToken) {
        this.accessToken = accessToken;
        this.refreshToken = null; // 또는 빈 문자열
    }
}
