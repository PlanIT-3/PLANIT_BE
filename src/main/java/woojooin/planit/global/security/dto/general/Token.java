package woojooin.planit.global.security.dto.general;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Data
public class Token {
    private Long id;
    private String refreshToken;
    private Long expiration;
}
