package woojooin.planit.global.util.codef.dto.account;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResDepositTrust {
    private String resAccount;
    private String resAccountDisplay;
    private String resAccountBalance;
    private String resAccountDeposit;
    private String resAccountNickName;
    private String resAccountStartDate;
    private String resAccountEndDate;
    private String resAccountName;
    private String resAccountCurrency;
    private String resAccountLifetime;
    private String resLastTranDate;
    private String resOverdraftAcctYN;
    private String resLoanKind;
    private String resLoanBalance;
    private String resLoanStartDate;
    private String resLoanEndDate;
}