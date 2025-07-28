package woojooin.planit.domain.account.isa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.account.isa.service.IsaAccountService;
import woojooin.planit.domain.member.domain.MemberProduct;

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
	public ResponseEntity<List<MemberProduct>> getIsaAccountProducts(
		@ApiParam(value = "회원 ID", required = true, example = "1")
		@PathVariable Long memberId) {

		List<MemberProduct> products = isaAccountService.getMemberProductsByMemberId(memberId);

		return ResponseEntity.ok(products);
	}
}