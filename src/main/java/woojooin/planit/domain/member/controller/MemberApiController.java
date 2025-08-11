package woojooin.planit.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import woojooin.planit.domain.member.dto.res.InvestScoreRes;
import woojooin.planit.domain.member.service.MemberService;
import woojooin.planit.global.response.Response;
import woojooin.planit.global.security.CustomUserDetails;

@RestController
@RequestMapping("/auth/api/member")
@RequiredArgsConstructor
@Api(value = "회원 API", description = "회원 관련 API")
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/test")
    @ApiOperation(value = "회원 테스트 API", notes = "회원 API 테스트")
    public String memberTest() {
        return "Member API is working!";
    }
    
    @GetMapping("/health")
    @ApiOperation(value = "회원 API태 확인", notes = "회원 API 서버 상태를 확인합니다.")
    public String health() {
        return "Member API OK";
    }


    @PostMapping("/invest-type")
    @ApiOperation(value = "회원 투자 성향 저장", notes = "로그인 유저의 투자 성향 저장")
    public ResponseEntity<Void> saveInvestType(
            @RequestParam String type,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long memberId = user.getId();
        memberService.updateInvestType(memberId, type);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/invest-score")
    @ApiOperation(value = "회원 투자 성향 점수 조회", notes = "회원 투자 성향 점수 조회")
    public ResponseEntity<Response<InvestScoreRes>> getInvestScore(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        InvestScoreRes investScore = memberService.getInvestScore(customUserDetails.getId());
        return ResponseEntity.ok(Response.ok(investScore));
    }
}