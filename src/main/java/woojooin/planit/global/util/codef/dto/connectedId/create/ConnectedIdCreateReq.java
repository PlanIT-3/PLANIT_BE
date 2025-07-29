package woojooin.planit.global.util.codef.dto.connectedId.create;

import java.util.List;

import woojooin.planit.global.util.codef.dto.connectedId.AccountDto;

public record ConnectedIdCreateReq(List<AccountDto> accountList) {

}
