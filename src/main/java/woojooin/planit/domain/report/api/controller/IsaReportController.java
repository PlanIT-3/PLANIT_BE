package woojooin.planit.domain.report.api.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import woojooin.planit.domain.report.domain.dto.AccountComparisonDTO;

import woojooin.planit.domain.report.domain.dto.IsaCumulativeTaxSavingDTO;
import woojooin.planit.domain.report.domain.dto.IsaTaxSavingStatusDTO;
import woojooin.planit.domain.report.service.ReportService;
import woojooin.planit.global.response.Response;
import woojooin.planit.global.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/isa/reports")
public class IsaReportController {

	private final ReportService reportService;

	@GetMapping("/tax-saving-status")
	@ApiOperation(value = "절세 현황 그래프" , notes = "사용자의 유형에 따라 절세 형황응 나타냅니다.")
	public Response<IsaTaxSavingStatusDTO> getTaxSavingStatus(
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		Long memberId = customUserDetails.getId();
		IsaTaxSavingStatusDTO dto = reportService.getTaxSavingStatus(memberId);
		return Response.ok(dto);
	}

	@GetMapping("/cumulative-tax-saving")
	@ApiOperation(value = "누적 절세 그래프" , notes = "분기별로 누적되는 절세량을 그래프로 나타냅니다.")
	public Response<List<IsaCumulativeTaxSavingDTO>> getCumulativeTaxSaving(
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		Long memberId = customUserDetails.getId();
		List<IsaCumulativeTaxSavingDTO> list = reportService.getCumulativeTaxSaving(memberId);
		return Response.ok(list);
	}


	@GetMapping("/account-comparison")
	@ApiOperation(value = "ISA vs 일반 계좌 세금 비교", notes = "회원별 원금, ISA 세금, 일반 계좌 세금, 절세 효과 및 절세율을 제공합니다.")
	public Response<AccountComparisonDTO> getAccountComparison(
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		Long memberId = customUserDetails.getId();
		AccountComparisonDTO dto = reportService.getAccountComparison(memberId);
		return Response.ok(dto);
	}

}
