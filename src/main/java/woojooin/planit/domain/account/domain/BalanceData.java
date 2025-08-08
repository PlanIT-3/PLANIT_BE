package woojooin.planit.domain.account.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceData {
	private Long memberId;
	private Long amount;
	private String createdAt;
	private String updatedAt;
}