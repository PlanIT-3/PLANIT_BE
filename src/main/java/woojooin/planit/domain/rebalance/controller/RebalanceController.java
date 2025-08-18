package woojooin.planit.domain.rebalance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import woojooin.planit.domain.rebalance.dto.res.RebalanceInvestInfoRes;
import woojooin.planit.domain.rebalance.dto.res.RebalanceRes;
import woojooin.planit.domain.rebalance.dto.res.RebalancingInfo;
import woojooin.planit.domain.rebalance.service.RebalanceService;
import woojooin.planit.global.response.Response;
import woojooin.planit.global.security.CustomUserDetails;
import woojooin.planit.global.util.calc.dto.RebalanceChoice;

@RestController
@RequiredArgsConstructor
public class RebalanceController {

	private final RebalanceService rebalanceService;

	@GetMapping("/auth/rebalance/recommend")
	public ResponseEntity<?> getRebalanceInfo(@AuthenticationPrincipal CustomUserDetails member) {

		List<RebalancingInfo> infos = rebalanceService.reqCurrentRebalancing(member.getId());

		RebalanceRes rebalanceRes = new RebalanceRes(infos);

		return ResponseEntity.ok(Response.ok(rebalanceRes));
	}

	@GetMapping("/auth/rebalance/invest/info")
	public ResponseEntity<List<RebalanceInvestInfoRes>> getRebalanceInvestInfo(
		@AuthenticationPrincipal CustomUserDetails member) {

		List<RebalanceInvestInfoRes> rebalanceInvestInfoRes = rebalanceService.getRebalanceInvestInfo(member.getId());

		return ResponseEntity.ok(rebalanceInvestInfoRes);
	}

	@GetMapping("/auth/rebalance/rate")
	public ResponseEntity<Response<?>> getRebalanceChoice(
		@AuthenticationPrincipal CustomUserDetails member) {

		List<RebalanceChoice> choiceList = rebalanceService.getRebalanceChoice(member.getId());
		return ResponseEntity.ok(Response.ok(choiceList));
	}

}
