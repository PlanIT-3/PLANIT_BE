package woojooin.planit.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import woojooin.planit.domain.member.domain.Member;
import woojooin.planit.domain.member.service.MemberService;
import woojooin.planit.global.exception.BusinessException;
import woojooin.planit.global.response.ResponseCode;
import woojooin.planit.global.repository.TokenRepository;

import java.math.BigInteger;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String SECRET;

    private long accessTokenExpirationMillis = 3600000;

    private long refreshTokenExpirationMillis= 604800000; // 7 days in milliseconds

//    private final long EXPIRATION = 1000L * 60 * 60;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private MemberService memberService;

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

    public String createValidatedAccessToken(Long userId, String requestedRole) {
        Member member = memberService.findById(userId);

        if (member == null) {
            throw new JwtException("User not found with ID: " + userId);
        }

        String finalRole = validateAndDetermineRole(member, requestedRole);
        String accessToken = createAccessToken(userId, finalRole);

        log.info("Validated access token created for user: {} with role: {}", userId, finalRole);
        return accessToken;
    }

    public String createValidatedRefreshToken(Long userId, String requestedRole) {
        Member member = memberService.findById(userId);

        if (member == null) {
            throw new JwtException("User not found with ID: " + userId);
        }

        String finalRole = validateAndDetermineRole(member, requestedRole);
        String refreshToken = createRefreshToken(userId, finalRole);

        log.info("Validated refresh token created for user: {} with role: {}", userId, finalRole);
        return refreshToken;
    }

    private String validateAndDetermineRole(Member member, String requestedRole) {
        String currentDbRole = member.getRole();
        String connectedId = member.getConnectedId();

        // connected_id가 있는 경우 (완전 회원)
        if (connectedId != null) {
            // SEMI_USER 요청은 불가
//            if ("SEMI_USER".equals(requestedRole)) {
//                log.warn("Invalid role request: User {} has connected_id but requested SEMI_USER",
//                        member.getMemberId());
//                throw new BusinessException(ResponseCode.INSUFFICIENT_PRIVILEGES);
//            }

            // DB에 SEMI_USER로 되어있으면 USER로 업그레이드
            if ("SEMI_USER".equals(requestedRole)) {
                log.info("Upgrading user {} from SEMI_USER to USER due to connected_id", member.getMemberId());
                member.setRole("USER");
                memberService.update(member);
                return "USER";
            }

            // 요청된 권한이 DB 권한과 일치하는지 확인
            if (!requestedRole.equals(currentDbRole)) {
                log.warn("Role mismatch: User {} requested {} but DB has {}",
                        member.getMemberId(), requestedRole, currentDbRole);
                throw new BusinessException(ResponseCode.ROLE_MISMATCH);
            }

            return currentDbRole;
        }
        // connected_id가 없는 경우 (반회원)
        else {
            // USER 이상의 권한 요청은 불가
            if (!"SEMI_USER".equals(requestedRole)) {
                log.warn("Invalid role request: User {} has no connected_id but requested {}",
                        member.getMemberId(), requestedRole);
                throw new BusinessException(ResponseCode.INSUFFICIENT_PRIVILEGES);
            }

//            // DB에 USER로 되어있으면 SEMI_USER로 다운그레이드
//            if (!"SEMI_USER".equals(currentDbRole)) {
//                log.info("User {} has no connected_id, maintaining SEMI_USER role", member.getMemberId());
//                 member.setRole("SEMI_USER");
//                 memberService.update(member);
//            }

            return "SEMI_USER";
        }
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


    // Refresh Token 삭제 (로그아웃 시)
    public void deleteRefreshToken(Long userId) {
        tokenRepository.deleteToken(userId);
    }
}
