package woojooin.planit.domain.member.domain;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.account.domain.Account;
import woojooin.planit.domain.goal.domain.vo.Goal;
import woojooin.planit.domain.report.domain.vo.DepositTaxSavingHistory;
import woojooin.planit.domain.report.domain.vo.IsaTaxSavingHistory;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
	private Long memberId;
	private String role;
	private String connectedId;
	private Integer rewardCnt;
	private String socialId;
	private String authVender;
	private String investType;
	private LocalDateTime lastVisit;
	private String email;
	private String password;
	private Boolean benefit;
	private String nickname;
	private Boolean isAgreed;
	private Double stable;
	private Double income;
	private Double liquid;
	private Double growth;
	private Double diversified;
	private String fcmToken;

	private List<MemberProduct> memberProducts;
	private List<Goal> goals;
	private List<Account> accounts;
	private List<IsaTaxSavingHistory> isaTaxSavingHistories;
	private List<DepositTaxSavingHistory> depositTaxSavingHistories;
}
