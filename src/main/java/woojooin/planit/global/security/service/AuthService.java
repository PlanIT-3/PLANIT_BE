package woojooin.planit.global.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import woojooin.planit.domain.member.domain.Member;
import woojooin.planit.domain.member.service.MemberService;
import woojooin.planit.global.exception.BusinessException;
import woojooin.planit.global.repository.TokenRepository;
import woojooin.planit.global.response.ResponseCode;
import woojooin.planit.global.security.CustomUserDetails;
import woojooin.planit.global.security.Role;
import woojooin.planit.global.security.dto.general.Token;
import woojooin.planit.global.security.dto.request.SignUpReq;
import woojooin.planit.global.security.dto.response.LoginRes;
import woojooin.planit.global.security.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
	private final AuthenticationManager authManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final TokenRepository tokenRepository;
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;

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
			String username = userDetails.getUsername();


			String accessToken = jwtTokenProvider.createValidatedAccessToken(userId, role);
			String refreshToken =   jwtTokenProvider.createValidatedRefreshToken(userId, role);
			// Redis에 Refresh Token 저장
			tokenRepository.saveToken(refreshToken, userId, refreshTokenExpirationMillis/ 1000);


			return new LoginRes(email, password, username, role, accessToken, refreshToken);
		} catch (AuthenticationException e) {
			throw new BusinessException(ResponseCode.INVALID_LOGIN);
		}
	}

	public ResponseEntity<String> signup(SignUpReq request) {
		if (memberService.findByEmail(request.getEmail()) != null) {
			throw new BusinessException(ResponseCode.DUPLICATE_EMAIL);
		}

		Member newMember = new Member();
		newMember.setEmail(request.getEmail());
		newMember.setPassword(passwordEncoder.encode(request.getPassword()));
		newMember.setNickname(request.getNickname());
		newMember.setRole(Role.SEMI_USER.name());
		newMember.setIsAgreed(true);

		memberService.save(newMember);
		return null;
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

