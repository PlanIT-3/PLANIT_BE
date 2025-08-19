package woojooin.planit.global.util.codef.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import woojooin.planit.global.util.codef.dto.CodefResponse;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodefSecuritiesAccountProductData {

    @JsonProperty("result")
    private CodefResponse.CodefResult result;

    @JsonProperty("data")
    private ResSecuritiesAccountProduct data;

    @JsonProperty("connectedId")
    private String connectedId;
}