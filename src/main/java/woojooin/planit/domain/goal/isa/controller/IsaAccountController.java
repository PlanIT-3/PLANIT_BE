package woojooin.planit.domain.goal.isa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.goal.isa.dto.req.IsaAccountProductEditListReq;
import woojooin.planit.domain.goal.isa.dto.req.IsaAccountProductRegisterListReq;
import woojooin.planit.domain.goal.isa.dto.res.IsaAccountProductRes;
import woojooin.planit.domain.goal.isa.dto.res.IsaAccountTaxExemptionRes;
import woojooin.planit.domain.goal.isa.service.IsaAccountService;
import woojooin.planit.global.response.Response;
import woojooin.planit.global.security.CustomUserDetails;

@RestController
@RequestMapping("/auth/api/account/isa")
@Api(value = "ISA 계좌 API", description = "ISA 계좌 관련 API")
@RequiredArgsConstructor
@Slf4j
public class IsaAccountController {

	private final IsaAccountService isaAccountService;

	@GetMapping
	@ApiOperation(value = "유저의 ISA 계좌 상품 리스트 조회 API",
		notes = "특정 회원의 모든 상품 정보를 조회합니다.")
	public ResponseEntity<Response<List<IsaAccountProductRes>>> getIsaAccountProducts(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		List<IsaAccountProductRes> products = isaAccountService.getMemberProductsByMemberId(customUserDetails.getId());

		return ResponseEntity.ok(Response.ok(products));
	}

	@GetMapping("/edit")
	@ApiOperation(value = "유저의 ISA 계좌 상품 리스트 조회 API",
		notes = "특정 회원의 특정 목적에 대한 상품 정보를 조회합니다.")
	public ResponseEntity<Response<List<IsaAccountProductRes>>> getIsaAccountProducts(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@ApiParam(value = "목적 ID", required = true, example = "1")
		@RequestParam("goalId") Long goalId) {

		List<IsaAccountProductRes> products = isaAccountService.findAllByMemberIdAndGoalId(customUserDetails.getId(), goalId);

		return ResponseEntity.ok(Response.ok(products));
	}

	@PostMapping()
	@ApiOperation(value = "유저의 ISA 계좌 상품 등록 API",
		notes = "특정 회원의 ISA 계좌 상품을 등록합니다.")
	public ResponseEntity<Response<Void>> registerIsaAccountProducts(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody IsaAccountProductRegisterListReq isaAccountProductRegisterListReq) {

		isaAccountService.registerMemberProductsByMemberId(customUserDetails.getId(), isaAccountProductRegisterListReq);

		return ResponseEntity.ok(Response.ok());
	}

	@PutMapping()
	@ApiOperation(value = "유저의 ISA 계좌 상품 수정 API",
		notes = "특정 회원의 ISA 계좌 상품을 수정합니다.")
	public ResponseEntity<Response<Void>> editIsaAccountProducts(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody IsaAccountProductEditListReq isaAccountProductEditListReq) {

		isaAccountService.editMemberProductsByMemberId(customUserDetails.getId(), isaAccountProductEditListReq);

		return ResponseEntity.ok(Response.ok());
	}

	@GetMapping("/tax")
	@ApiOperation(value = "유저의 ISA 비과세 조회 API",
		notes = "특정 회원의 ISA 비과세 조회합니다.")
	public ResponseEntity<Response<IsaAccountTaxExemptionRes>> getIsaAccountTaxExemption(
		@AuthenticationPrincipal CustomUserDetails customUserDetails) {

		IsaAccountTaxExemptionRes isaAccountTaxExemptionByMemberId = isaAccountService.getIsaAccountTaxExemptionByMemberId(
			customUserDetails.getId());

		return ResponseEntity.ok(Response.ok(isaAccountTaxExemptionByMemberId));
	}
}