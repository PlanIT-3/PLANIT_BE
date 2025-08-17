package woojooin.planit.domain.rebalance.controller;

import java.util.ArrayList;
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

@RestController
@RequiredArgsConstructor
public class RebalanceController {

	private final RebalanceService rebalanceService;

	@GetMapping("/auth/rebalance")
	public ResponseEntity<?> getRebalanceInfo(@AuthenticationPrincipal CustomUserDetails member) {

		List<RebalancingInfo> infos = new ArrayList<>();
		infos = rebalanceService.reqCurrentRebalancing(member.getId());

		RebalanceRes rebalanceRes = new RebalanceRes(infos);

		return ResponseEntity.ok(Response.ok(rebalanceRes));
	}

	@GetMapping("/auth/rebalance/invest/info")
	public ResponseEntity<List<RebalanceInvestInfoRes>> getRebalanceInvestInfo(@AuthenticationPrincipal CustomUserDetails member) {

		List<RebalanceInvestInfoRes> rebalanceInvestInfoRes = rebalanceService.getRebalanceInvestInfo(member.getId());

		return ResponseEntity.ok(rebalanceInvestInfoRes);
	}



}
