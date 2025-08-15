package woojooin.planit.global.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.member.domain.Member;
import woojooin.planit.domain.member.repository.MemberRepository;
import woojooin.planit.domain.member.service.MemberService;
import woojooin.planit.global.security.CustomUserDetailsService;

/**
 * JWT 토큰 필터
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final String AUTHORIZATION_HEADER = "Authorization";
	private final String BEARER_PREFIX = "Bearer ";

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private MemberService memberService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain)
			throws ServletException, IOException {

		log.info("Processing JWT authentication for request: {}", request.getRequestURI());


//      if ( request.getRequestURI().startsWith("/api")) {
//         log.info("Bypassing JWT filter for URI: {}", request.getRequestURI());
//         filterChain.doFilter(request, response);
//         return;
//      }

		String token = resolveToken(request);

		if (token != null ) {
			try {
				jwtTokenProvider.validateToken(token);

				String username = jwtTokenProvider.getUsername(token);
				String role = jwtTokenProvider.getRole(token);
				Long userId = jwtTokenProvider.getUserId(token);

				Member member = memberService.findById(userId);

				if (member == null) {
					log.warn("Member not found for userId: {}", userId);
					sendErrorResponse(response, "USER_NOT_FOUND", "사용자를 찾을 수 없습니다!!!!!");
					return;
				}

				// connected_id가 있는데 토큰 role이 SEMI_USER인 경우 체크
				if (member.getConnectedId() != null && "SEMI_USER".equals(role)) {
					log.warn("Invalid token: User {} has connected_id but token role is SEMI_USER", userId);
					sendErrorResponse(response, "INVALID_TOKEN_ROLE", "토큰의 권한이 유효하지 않습니다.");
					log.info("User {} has connected_id but token role is SEMI_USER", userId);
					return;
				}

				// 토큰의 role과 DB의 role이 일치하는지 체크
				if (!role.equals(member.getRole())) {
					log.warn("Token role mismatch: userId={}, token={}, db={}", userId, role, member.getRole());
					sendErrorResponse(response, "ROLE_MISMATCH", "토큰의 권한이 최신 상태가 아닙니다.");
					return;
				}

				UserDetails userDetails = userDetailsService.loadUserByMemberId(userId);

				UsernamePasswordAuthenticationToken auth =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (JwtException e) {
				log.error("JWT authentication error: {}", e.getMessage());
				sendErrorResponse(response, "JWT_AUTH_ERROR", "JWT 인증 오류가 발생했습니다.");
				return;
			} catch (Exception e) {
				log.error("Unexpected error during JWT authentication: {}", e.getMessage());
				sendErrorResponse(response, "UNEXPECTED_ERROR", "예상치 못한 오류가 발생했습니다.");
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearer = request.getHeader(AUTHORIZATION_HEADER);
		if (bearer != null && bearer.startsWith(BEARER_PREFIX)) {
			return bearer.substring(7);
		}
		return null;
	}

	private void sendErrorResponse(HttpServletResponse response, String errorCode, String message) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(String.format(
				"{\"error\":\"%s\",\"message\":\"%s\"}",
				errorCode, message
		));
	}
}