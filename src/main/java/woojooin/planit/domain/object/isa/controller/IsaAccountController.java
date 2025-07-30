package woojooin.planit.domain.object.isa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.object.isa.dto.req.IsaAccountProductRegisterListReq;
import woojooin.planit.domain.object.isa.dto.res.IsaAccountProductRes;
import woojooin.planit.domain.object.isa.service.IsaAccountService;
import woojooin.planit.global.response.Response;

@RestController
@RequestMapping("/api/account/isa")
@Api(value = "ISA 계좌 API", description = "ISA 계좌 관련 API")
@RequiredArgsConstructor
@Slf4j
public class IsaAccountController {

	private final IsaAccountService isaAccountService;

	@GetMapping("/{memberId}")
	@ApiOperation(value = "유저의 ISA 계좌 상품 리스트 조회 API",
		notes = "특정 회원의 모든 상품 정보를 조회합니다.")
	public ResponseEntity<Response<List<IsaAccountProductRes>>> getIsaAccountProducts(
		@ApiParam(value = "회원 ID", required = true, example = "1")
		@PathVariable Long memberId) {

		List<IsaAccountProductRes> products = isaAccountService.getMemberProductsByMemberId(memberId);

		Response<List<IsaAccountProductRes>> response = Response.ok(products);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{memberId}")
	@ApiOperation(value = "유저의 ISA 계좌 상품 등록 API",
		notes = "특정 회원의 ISA 계좌 상품을 등록합니다.")
	public ResponseEntity<Response<Void>> registerIsaAccountProducts(
		@ApiParam(value = "회원 ID", required = true, example = "1")
		@PathVariable Long memberId,
		@RequestBody IsaAccountProductRegisterListReq isaAccountProductRegisterListReq) {

		isaAccountService.registerMemberProductsByMemberId(memberId, isaAccountProductRegisterListReq);

		return ResponseEntity.ok(Response.ok(null));
	}
}