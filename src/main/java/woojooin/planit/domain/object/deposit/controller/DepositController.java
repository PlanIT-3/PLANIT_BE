package woojooin.planit.domain.object.deposit.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import woojooin.planit.domain.object.deposit.dto.req.DepositProductEditListReq;
import woojooin.planit.domain.object.deposit.dto.req.DepositProductRegisterListReq;
import woojooin.planit.domain.object.deposit.dto.res.DepositProductRes;
import woojooin.planit.domain.object.deposit.service.DepositService;
import woojooin.planit.global.response.Response;

@RestController
@RequestMapping("/api/account/deposit")
@Api(value = "예적금 계좌 API", description = "예적금 계좌 관련 API")
@RequiredArgsConstructor
@Slf4j
public class DepositController {

	private final DepositService depositService;

	@GetMapping("/{memberId}")
	@ApiOperation(value = "유저의 예적금 계좌 상품 리스트 조회 API",
		notes = "특정 회원의 모든 예적금 상품 정보를 조회합니다.")
	public Response<List<DepositProductRes>> getDepositProducts(
		@ApiParam(value = "회원 ID", required = true, example = "1")
		@PathVariable Long memberId) {

		List<DepositProductRes> products = depositService.getMemberProductsByMemberId(memberId);

		return Response.ok(products);
	}

	@GetMapping("/edit/{memberId}")
	@ApiOperation(value = "유저의 예적금 계좌 상품 리스트 조회 API",
		notes = "특정 회원의 특정 목적에 대한 예적금 상품 정보를 조회합니다.")
	public Response<List<DepositProductRes>> getDepositProducts(
		@ApiParam(value = "회원 ID", required = true, example = "1")
		@PathVariable Long memberId,
		@ApiParam(value = "목적 ID", required = true, example = "1")
		@RequestParam("objectId") Long objectId) {

		List<DepositProductRes> products = depositService.findAllByMemberIdAndObjectId(memberId, objectId);

		return Response.ok(products);
	}

	@PostMapping("/{memberId}")
	@ApiOperation(value = "유저의 예적금 계좌 상품 등록 API",
		notes = "특정 회원의 예적금 계좌 상품을 등록합니다.")
	public Response<Void> registerDepositProducts(
		@ApiParam(value = "회원 ID", required = true, example = "1")
		@PathVariable Long memberId,
		@RequestBody DepositProductRegisterListReq depositProductRegisterListReq) {

		depositService.registerMemberProductsByMemberId(memberId, depositProductRegisterListReq);

		return Response.ok();
	}

	@PutMapping("/{memberId}")
	@ApiOperation(value = "유저의 예적금 계좌 상품 수정 API",
		notes = "특정 회원의 예적금 계좌 상품을 수정합니다.")
	public Response<Void> editDepositProducts(
		@ApiParam(value = "회원 ID", required = true, example = "1")
		@PathVariable Long memberId,
		@RequestBody DepositProductEditListReq depositProductEditListReq) {

		depositService.editMemberProductsByMemberId(memberId, depositProductEditListReq);

		return Response.ok();
	}
}