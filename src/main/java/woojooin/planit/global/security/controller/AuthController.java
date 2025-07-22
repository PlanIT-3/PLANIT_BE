package woojooin.planit.global.security.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import woojooin.planit.domain.member.domain.Member;
import woojooin.planit.domain.member.repository.MemberRepository;
import woojooin.planit.global.security.Role;
import woojooin.planit.global.security.dto.request.LoginReq;
import woojooin.planit.global.security.dto.request.SignUpReq;
import woojooin.planit.global.security.dto.response.LoginRes;
import woojooin.planit.global.security.jwt.JwtTokenProvider;
import woojooin.planit.global.security.service.AuthService;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq request) {
        LoginRes res = authService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(res);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpReq request) {
        log.info("Signup request: {}", request);
        if (memberRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        Member newMember = new Member();
        newMember.setEmail(request.getEmail());
        newMember.setPassword(passwordEncoder.encode(request.getPassword()));
        newMember.setNickname(request.getNickname());
        newMember.setRole(Role.SEMI_USER.name());

        memberRepository.save(newMember);
        return ResponseEntity.ok().build();
    }

}
