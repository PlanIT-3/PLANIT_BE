package woojooin.planit.domain.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import woojooin.planit.domain.account.dto.res.BalanceListRes;
import woojooin.planit.domain.account.service.AccountService;
import woojooin.planit.global.response.Response;

@RestController
@RequestMapping("/auth/api/account")
@RequiredArgsConstructor
@Api(value = "계좌 API" ,description = "계좌 설정 및 조회 관련 API ")
public class AccountController {

	private final AccountService accountService;

	@GetMapping("/{period}")
	@ApiOperation(value = "기간별 잔고 조회" , notes = "사용자의 기간별 잔고를 조회합니다. period: day(일주일), week(6주), month(6개월)" )
	public ResponseEntity<Response<BalanceListRes>> getAccountBalanceForDate(@PathVariable String period) {

		BalanceListRes balance = accountService.getAccountBalanceForDate(1L, period);
		return ResponseEntity.ok(Response.ok(balance));
	}
}
