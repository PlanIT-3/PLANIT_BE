package woojooin.planit.domain.account.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountBankRes {
	private String bankCode;
	private String accountNumber;
}