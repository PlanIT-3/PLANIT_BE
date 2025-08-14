package woojooin.planit.domain.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.account.dto.req.AccountListRequest;
import woojooin.planit.domain.account.dto.res.AccountsRes;
import woojooin.planit.domain.account.dto.res.BalanceListRes;
import woojooin.planit.domain.account.service.AccountService;
import woojooin.planit.domain.goal.dto.res.GoalRatioListRes;
import woojooin.planit.global.response.Response;
import woojooin.planit.global.security.CustomUserDetails;
import woojooin.planit.global.security.dto.response.AccountConnectionRes;
import woojooin.planit.global.util.codef.dto.connectedId.create.ConntectedIdCreateRes;
import woojooin.planit.global.util.codef.dto.token.CodefTokenRes;

@Slf4j
@RestController
@RequestMapping("/auth/api/account")
@RequiredArgsConstructor
@Api(value = "계좌 API", description = "계좌 설정 및 조회 관련 API ")
public class AccountController {

	private final AccountService accountService;


	@GetMapping("/{period}")
	@ApiOperation(value = "기간별 잔고 조회", notes = "사용자의 기간별 잔고를 조회합니다. period: day(일주일), week(6주), month(6개월)")
	public ResponseEntity<Response<BalanceListRes>> getAccountBalanceForDate(
		@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable String period) {

		BalanceListRes balance = accountService.getAccountBalanceForDate(customUserDetails.getId(), period);
		return ResponseEntity.ok(Response.ok(balance));
	}

	@GetMapping("/goal-ratio")
	@ApiOperation(value = "총 재산에서 목적별 비중 조회", notes = "총 재산에서 목적별 비중을 조회합니다.")
	public ResponseEntity<Response<GoalRatioListRes>> getGoalRatioBasedOnAccount(
		@AuthenticationPrincipal CustomUserDetails customUserDetails
		) {

		GoalRatioListRes goalRatioList = accountService.getGoalRatioBasedOnAccount(customUserDetails.getId());
		return ResponseEntity.ok(Response.ok(goalRatioList));
	}

	@PostMapping("/register")
	@ApiOperation(value = "계좌 등록", notes = "사용자의 계좌를 등록합니다.")
	public ResponseEntity<Response<AccountConnectionRes>> registerAccount(
		@RequestBody AccountListRequest request,
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		AccountConnectionRes result = accountService.register(request.getAccountDto(), request.isLast(), customUserDetails.getId());
		return ResponseEntity.ok(Response.ok(result));
	}

	@GetMapping()
	@ApiOperation(value = "계좌 리스트 조회", notes = "사용자의 계좌들을 조회합니다.")
	public ResponseEntity<Response<AccountsRes>> getAccountList(
		@AuthenticationPrincipal CustomUserDetails customUserDetails
	) {
		AccountsRes accountList = accountService.getAccountList(customUserDetails.getId());
		return ResponseEntity.ok(Response.ok(accountList));
	}
}