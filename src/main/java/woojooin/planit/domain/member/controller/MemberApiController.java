package woojooin.planit.domain.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/member")
@Api(value = "회원 API", description = "회원 관련 API")
public class MemberApiController {
    
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
}