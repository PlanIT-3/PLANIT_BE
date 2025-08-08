package woojooin.planit.global.security.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import woojooin.planit.domain.member.domain.Member;
import woojooin.planit.domain.member.service.MemberService;
import woojooin.planit.global.exception.BusinessException;
import woojooin.planit.global.response.ResponseCode;
import woojooin.planit.global.security.Role;
import woojooin.planit.global.security.dto.request.LoginReq;
import woojooin.planit.global.security.dto.request.RefreshTokenReq;
import woojooin.planit.global.security.dto.request.SignUpReq;
import woojooin.planit.global.security.dto.response.LoginRes;
import woojooin.planit.global.security.service.AuthService;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq request) {
        LoginRes res = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpReq request) {
        authService.signup(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody RefreshTokenReq refreshTokenReq) {
        try {
            String newAccessToken = authService.reissueAccessToken(refreshTokenReq.getRefreshToken());
            return ResponseEntity.ok(new LoginRes(newAccessToken));
        } catch (AuthenticationException e) {
            log.error("Reissue failed: {}", e.getMessage());
            throw new BusinessException(ResponseCode.REISSUE_FAILED);
        }
    }
}
