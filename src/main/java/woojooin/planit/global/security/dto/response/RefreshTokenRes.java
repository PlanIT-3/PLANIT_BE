package woojooin.planit.global.security.dto.response;
public class RefreshTokenRes {
    private String refreshToken;

    public RefreshTokenRes(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken(){return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

