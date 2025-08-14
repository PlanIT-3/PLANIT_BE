package woojooin.planit.global.util.codef.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResSecuritiesAccount {
    
    @JsonProperty("resAccount")
    private String resAccount;
    
    @JsonProperty("resAccountDisplay")
    private String resAccountDisplay;
    
    @JsonProperty("resAccountName")
    private String resAccountName;
    
    @JsonProperty("resAccountNickName")
    private String resAccountNickName;
    
    @JsonProperty("resPrincipal")
    private String resPrincipal;
    
    @JsonProperty("resPurchaseAmount")
    private String resPurchaseAmount;
    
    @JsonProperty("resLoanAmt")
    private String resLoanAmt;
    
    @JsonProperty("resValuationAmt")
    private String resValuationAmt;
    
    @JsonProperty("resValuationPL")
    private String resValuationPL;
    
    @JsonProperty("resWithdrawalAmt")
    private String resWithdrawalAmt;
    
    @JsonProperty("resDepositReceived")
    private String resDepositReceived;
    
    @JsonProperty("resDepositReceivedD1")
    private String resDepositReceivedD1;
    
    @JsonProperty("resDepositReceivedD2")
    private String resDepositReceivedD2;
    
    @JsonProperty("resDepositReceivedF")
    private String resDepositReceivedF;
    
    @JsonProperty("resEarningsRate")
    private String resEarningsRate;
}