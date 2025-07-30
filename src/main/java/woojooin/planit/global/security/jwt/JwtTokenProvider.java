package woojooin.planit.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import woojooin.planit.global.security.dto.general.Token;
import woojooin.planit.global.repository.TokenRepository;

import java.math.BigInteger;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.access-token-expiration-mills}")
    private long accessTokenExpirationMillis;

    @Value("${jwt.refresh-token-expiration-mills}")
    private long refreshTokenExpirationMillis;

//    private final long EXPIRATION = 1000L * 60 * 60;

    @Autowired
    private TokenRepository tokenRepository;

    // Access Token 생성
    public String createAccessToken(Long userId, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpirationMillis);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(Long userId, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenExpirationMillis);

        String refreshToken = Jwts.builder()
                .setSubject(userId.toString())
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes())
                .compact();

        // Redis에 Refresh Token 저장
        tokenRepository.saveToken(refreshToken, userId, refreshTokenExpirationMillis / 1000);

        return refreshToken;
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Long getUserId(String token) {
        String subject = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return Long.parseLong(subject); // subject를 Long으로 변환
    }

    public String getRole(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }



    // Refresh Token으로 새 Access Token 발급
    public String refreshAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new JwtException("Invalid refresh token");
        }

        Long userId = getUserId(refreshToken);
        // Redis에서 해당 refresh token이 존재하는지 확인
        Token storedToken = tokenRepository.findToken(Long.valueOf(userId));
        if (storedToken == null || !storedToken.getRefreshToken().equals(refreshToken)) {
            throw new JwtException("Refresh token not found or invalid");
        }

        return createAccessToken(userId, getRole(refreshToken));
    }

    // Refresh Token 삭제 (로그아웃 시)
    public void deleteRefreshToken(Long userId) {
        tokenRepository.deleteToken(userId);
    }
}

