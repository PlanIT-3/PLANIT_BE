package woojooin.planit.global.util.codef.dto.account;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CodefAccountData {
    private List<ResDepositTrust> resDepositTrust;
    private List<Object> resForeignCurrency;
    private List<Object> resFund;
    private List<Object> resLoan;
    private List<Object> resInsurance;
}