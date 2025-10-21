package woojooin.planit.domain.goal.domain.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.account.domain.Account;
import woojooin.planit.domain.goal.domain.enums.ActionType;
import woojooin.planit.domain.member.domain.MemberProduct;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Action {

	private Long actionId;

	private Long goalId;

	private Long accountId;

	private String accountNumber;

	private Long memberProductId;

	private Integer allocatedRate;

	private Integer accountAllocatedRate;

	private ActionType accountType;

	private Integer amount;

	private ActionType actionType;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private Goal goal;
	private Account account;
	private MemberProduct memberProduct;
}