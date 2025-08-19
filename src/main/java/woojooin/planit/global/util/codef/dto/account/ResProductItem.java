package woojooin.planit.global.util.codef.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResProductItem {

    @JsonProperty("resProductType")
    private String resProductType;

    @JsonProperty("resProductTypeCd")
    private String resProductTypeCd;

    @JsonProperty("resItemName")
    private String resItemName;

    @JsonProperty("resItemCode")
    private String resItemCode;

    @JsonProperty("resBalanceType")
    private String resBalanceType;

    @JsonProperty("resQuantity")
    private String resQuantity;

    @JsonProperty("resSettleQuantity")
    private String resSettleQuantity;

    @JsonProperty("resPresentAmt")
    private String resPresentAmt;

    @JsonProperty("resAvgPresentAmt")
    private String resAvgPresentAmt;

    @JsonProperty("resPurchaseAmount")
    private String resPurchaseAmount;

    @JsonProperty("resValuationAmt")
    private String resValuationAmt;

    @JsonProperty("resValuationPL")
    private String resValuationPL;

    @JsonProperty("resEarningsRate")
    private String resEarningsRate;

    @JsonProperty("resAccountCurrency")
    private String resAccountCurrency;
}