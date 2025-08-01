package woojooin.planit.global.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import woojooin.planit.global.repository.TokenRepository;
import woojooin.planit.global.security.CustomUserDetails;
import woojooin.planit.global.security.dto.general.Token;
import woojooin.planit.global.security.dto.response.LoginRes;
import woojooin.planit.global.security.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    @Value("${jwt.refresh-token-expiration-mills}")
    private long refreshTokenExpirationMillis;

    public LoginRes login(String email, String password) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            Long userId = userDetails.getId();
            String role = userDetails.getRole();

            String accessToken = jwtTokenProvider.createValidatedAccessToken(userId, role);
            String refreshToken =   jwtTokenProvider.createValidatedRefreshToken(userId, role);

            // Redis에 Refresh Token 저장
            tokenRepository.saveToken(refreshToken, userId, refreshTokenExpirationMillis/ 1000);


            return new LoginRes(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    public String reissueAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        Long userId = jwtTokenProvider.getUserId(refreshToken);
        String role = jwtTokenProvider.getRole(refreshToken);
        log.info("Reissuing access token for userId: {}, role: {}", userId, role);

        Token storedRefreshToken = tokenRepository.findToken(userId);
        log.info("Stored refresh token for userId {}: {}", userId, storedRefreshToken);
        if (storedRefreshToken == null || !storedRefreshToken.getRefreshToken().equals(refreshToken)) {
            throw new BadCredentialsException("Refresh token does not match stored token");
        }
        return jwtTokenProvider.createValidatedAccessToken(userId, role);
    }

}

