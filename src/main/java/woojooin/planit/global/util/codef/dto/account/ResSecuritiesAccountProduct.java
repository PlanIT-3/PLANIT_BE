package woojooin.planit.global.util.codef.dto.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResSecuritiesAccountProduct {

    @JsonProperty("resAccount")
    private String resAccount;

    @JsonProperty("resItemList")
    private List<ResProductItem> resItemList;

    @JsonProperty("resDepositReceived")
    private String resDepositReceived;
}