package woojooin.planit.global.util.codef.dto.account;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountListDto {
    private String accountNumber;
    private String accountBalance;
    private String accountDeposit;
    private String accountName;
    private String accountCurrency;
    private String accountStartDate;
    private String accountDisplay;
    private String accountEndDate;
}