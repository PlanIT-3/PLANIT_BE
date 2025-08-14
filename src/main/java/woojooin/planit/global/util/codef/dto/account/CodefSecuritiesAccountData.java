package woojooin.planit.global.util.codef.dto.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import woojooin.planit.global.util.codef.dto.CodefResponse.CodefResult;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodefSecuritiesAccountData {
    
    @JsonProperty("result")
    private CodefResult result;
    
    @JsonProperty("data")
    private List<ResSecuritiesAccount> data;
    
    @JsonProperty("connectedId")
    private String connectedId;
}