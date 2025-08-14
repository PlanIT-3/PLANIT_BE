package woojooin.planit.global.util.codef.dto.account;

import lombok.Getter;
import woojooin.planit.global.util.codef.dto.CodefResponse;

@Getter
public class CodefAccountResponse extends CodefResponse<CodefAccountData> {
    private String connectedId;
}