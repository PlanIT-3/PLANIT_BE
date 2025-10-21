package woojooin.planit.domain.account.api.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountListRes {
	private String organization;
	private String accountNumber;
}