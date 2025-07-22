package woojooin.planit.global.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import woojooin.planit.global.security.dto.response.LoginRes;
import woojooin.planit.global.security.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final AuthenticationManager authManager;
	private final JwtTokenProvider jwtTokenProvider;

	public LoginRes login(String email, String password) {
		try {
			Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, password)
			);

			String accessToken = jwtTokenProvider.createToken(email);

			return new LoginRes(accessToken);
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid email or password");
		}
	}

}
