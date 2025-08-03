package woojooin.planit.domain.object.deposit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
import woojooin.planit.domain.object.deposit.dto.req.DepositAccountEditListReq;
import woojooin.planit.domain.object.deposit.dto.req.DepositAccountRegisterListReq;
import woojooin.planit.domain.object.deposit.dto.res.DepositAccountRes;
import woojooin.planit.domain.object.deposit.service.DepositService;
import woojooin.planit.global.response.Response;

@RestController
@RequestMapping("/auth/api/account/deposit")
@Api(value = "예적금 계좌 API", description = "예적금 계좌 관련 API")
@RequiredArgsConstructor
@Slf4j
public class DepositController {

	private final DepositService depositService;

	@GetMapping("/{memberId}")
	@ApiOperation(value = "유저의 예적금 계좌 리스트 조회 API",
		notes = "특정 회원의 모든 예적금 계좌 정보를 조회합니다.")
	public ResponseEntity<Response<List<DepositAccountRes>>> getDepositAccounts(
		@ApiParam(value = "회원 ID", required = true, example = "1")
		@PathVariable Long memberId) {

		List<DepositAccountRes> accounts = depositService.getMemberAccountsByMemberId(memberId);

		return ResponseEntity.ok(Response.ok(accounts));
	}

	@GetMapping("/edit/{memberId}")
	@ApiOperation(value = "특정 목적에 할당된 예적금 계좌 조회 API",
		notes = "특정 목적에 할당된 예적금 계좌 정보를 조회합니다.")
	public ResponseEntity<Response<List<DepositAccountRes>>> getDepositAccountsByGoalId(
		@ApiParam(value = "회원 ID", required = true, example = "1")
		@PathVariable Long memberId,
		@ApiParam(value = "목적 ID", required = true, example = "1")
		@RequestParam("goalId") Long goalId) {

		List<DepositAccountRes> accounts = depositService.findAllByMemberIdAndObjectId(memberId, goalId);

		return ResponseEntity.ok(Response.ok(accounts));
	}

	@GetMapping("/available/{memberId}")
	@ApiOperation(value = "할당 가능한 예적금 계좌 조회 API",
		notes = "아직 할당되지 않았거나 부분 할당된 예적금 계좌들을 조회합니다.")
	public ResponseEntity<Response<List<DepositAccountRes>>> getAvailableDepositAccounts(
		@ApiParam(value = "회원 ID", required = true, example = "1")
		@PathVariable Long memberId,
		@ApiParam(value = "목적 ID", required = true, example = "1")
		@RequestParam("goalId") Long goalId) {

		List<DepositAccountRes> accounts = depositService.findAvailableAccountsByMemberIdAndObjectId(memberId, goalId);

		return ResponseEntity.ok(Response.ok(accounts));
	}

	@PostMapping("/{memberId}")
	@ApiOperation(value = "유저의 예적금 계좌 등록 API",
		notes = "특정 회원의 예적금 계좌를 등록합니다.")
	public ResponseEntity<Response<Void>> registerDepositAccounts(
		@ApiParam(value = "회원 ID", required = true, example = "1")
		@PathVariable Long memberId,
		@RequestBody DepositAccountRegisterListReq depositAccountRegisterListReq) {

		depositService.registerMemberAccountsByMemberId(memberId, depositAccountRegisterListReq);

		return ResponseEntity.status(201).body(Response.ok());
	}

	@PutMapping("/{memberId}")
	@ApiOperation(value = "유저의 예적금 계좌 수정 API",
		notes = "특정 회원의 예적금 계좌를 수정합니다.")
	public ResponseEntity<Response<Void>> editDepositAccounts(
		@ApiParam(value = "회원 ID", required = true, example = "1")
		@PathVariable Long memberId,
		@RequestBody DepositAccountEditListReq depositAccountEditListReq) {

		depositService.editMemberAccountsByMemberId(memberId, depositAccountEditListReq);

		return ResponseEntity.ok(Response.ok());
	}
}