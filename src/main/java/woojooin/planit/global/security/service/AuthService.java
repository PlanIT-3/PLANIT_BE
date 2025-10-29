package woojooin.planit.global.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
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

import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
	private final AuthenticationManager authManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final TokenRepository tokenRepository;
	private final MemberService memberService;
	private final PasswordEncoder passwordEncoder;
	private final JavaMailSender mailSender;

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


			return new LoginRes(userId, email, password, username, role, accessToken, refreshToken);
		} catch (AuthenticationException e) {
			throw new BusinessException(ResponseCode.INVALID_LOGIN);
		}
	}

	public ResponseEntity<String> signup(SignUpReq request) {
		if (memberService.findByEmail(request.getEmail()) != null) {
			throw new BusinessException(ResponseCode.DUPLICATE_EMAIL);
		}

		String authKey = UUID.randomUUID().toString();

		Member newMember = new Member();
		newMember.setEmail(request.getEmail());
		newMember.setPassword(passwordEncoder.encode(request.getPassword()));
		newMember.setNickname(request.getNickname());
		newMember.setRole(Role.SEMI_USER.name());
		newMember.setIsAgreed(true);
		newMember.setAuthKey(authKey);
		newMember.setAuthStatus(0);

		memberService.save(newMember);

		try {
			sendVerificationEmail(newMember.getEmail(), authKey);
		} catch (Exception e) {
			log.error("Failed to send verification email", e);
			throw new BusinessException(ResponseCode.EMAIL_SEND_FAILED);
		}
		return null;
	}

	private void sendVerificationEmail(String email, String authKey) throws Exception {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setSubject("[PlanIt] 회원가입 인증 메일입니다.");
		helper.setTo(email);

		String url = "http://localhost:8080/api/auth/confirm-email?email=" + email + "&authKey=" + authKey;

		String htmlContent = "<h2>회원가입을 완료하려면 아래 링크를 클릭하세요.</h2>"
				+ "<p>인증 링크: <a href='" + url + "'>이메일 인증하기</a></p>";

		helper.setText(htmlContent, true);

		mailSender.send(message);
	}

	@Transactional
	public void confirmEmail(String email, String authKey) throws AuthenticationException {
		Member member = memberService.findByEmail(email);

		if (member.getAuthStatus() == 1) {
			log.warn("Email already verified: {}", email);
			return;
		}

		member.setAuthStatus(1);
		memberService.update(member);
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

