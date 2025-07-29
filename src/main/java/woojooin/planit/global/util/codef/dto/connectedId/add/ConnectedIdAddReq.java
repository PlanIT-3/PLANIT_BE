package woojooin.planit.global.util.codef.dto.connectedId.add;

import java.util.List;

import woojooin.planit.global.util.codef.dto.connectedId.AccountDto;

public record ConnectedIdAddReq(List<AccountDto> accountList, String connectedId) {
}
