package woojooin.planit.domain.report.controller;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import woojooin.planit.domain.report.domain.ReturnRateDto;
import woojooin.planit.domain.report.domain.ReturnType;
import woojooin.planit.domain.report.service.ReportService;
import woojooin.planit.global.response.Response;
import woojooin.planit.global.security.CustomUserDetails;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("returns")
    @ApiOperation(value = "수익률 막대그래프 조회" , notes = "일별 , 주간 , 월별 수익률 데이터를 조회합니다.")
    public  ResponseEntity<Response<List<ReturnRateDto>>> getReturnRateByType(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("type")ReturnType returnType,
            @RequestParam(value = "startDate" , required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate){

        Long memberId = userDetails.getId();
        List<ReturnRateDto> returnRates = reportService.getReturnRateByType(memberId, returnType, startDate);
        return ResponseEntity.ok(Response.ok(returnRates));
    }
}









