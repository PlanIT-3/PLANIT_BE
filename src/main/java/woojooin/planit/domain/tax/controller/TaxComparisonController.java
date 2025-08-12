package woojooin.planit.domain.tax.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import woojooin.planit.domain.tax.dto.TaxComparisonRes;
import woojooin.planit.domain.tax.service.TaxComparisonService;
import woojooin.planit.global.response.Response;
import woojooin.planit.global.response.ResponseCode;

@Slf4j
@RestController
@RequestMapping("/api/tax")
@RequiredArgsConstructor
@Api(value = "Tax Comparison API", description = "세금 비교 관련 API")
public class TaxComparisonController {
    
    private final TaxComparisonService taxComparisonService;
    
    @GetMapping("/comparison/{memberId}/trend")
    @ApiOperation(value = "세금 시계열 비교", notes = "특정 회원의 분기별 세금 비교 시계열 데이터를 조회합니다.")
    public ResponseEntity<Response<TaxComparisonRes>> getTaxTrend(
            @ApiParam(value = "회원 ID", required = true) @PathVariable Long memberId,
            @ApiParam(value = "조회할 분기 수", required = false) @RequestParam(defaultValue = "8") Integer quarters) {
        
        log.info("Tax trend request for member: {}, quarters: {}", memberId, quarters);
        
        try {
            TaxComparisonRes response = taxComparisonService.getTaxTrend(memberId, quarters);
            return ResponseEntity.ok(Response.ok(response));
        } catch (Exception e) {
            log.error("Error occurred while getting tax trend for member: {}", memberId, e);
            return ResponseEntity.internalServerError()
                .body(Response.<TaxComparisonRes>build(null, ResponseCode.INTERNAL_ERROR));
        }
    }
}