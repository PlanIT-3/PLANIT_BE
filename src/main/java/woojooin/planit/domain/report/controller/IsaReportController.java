package woojooin.planit.domain.report.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import woojooin.planit.domain.report.domain.IsaTaxSavingStatusDTO;
import woojooin.planit.domain.report.domain.ReturnRateDto;
import woojooin.planit.domain.report.domain.ReturnType;
import woojooin.planit.domain.report.service.ReportService;
import woojooin.planit.global.response.Response;
import woojooin.planit.global.security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/isa/reports")
public class IsaReportController {

	private final ReportService reportService;

	@GetMapping("/tax-saving-status")
	@ApiOperation(value = "수익률 막대그래프 조회" , notes = "일별 , 주간 , 월별 수익률 데이터를 조회합니다.")
	public Response<IsaTaxSavingStatusDTO> getTaxSavingStatus(
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		Long memberId = customUserDetails.getId();
		IsaTaxSavingStatusDTO dto = reportService.getTaxSavingStatus(memberId);
		return Response.ok(dto);
	}
}
