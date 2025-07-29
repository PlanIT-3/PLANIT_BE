package woojooin.planit.global.util.codef.dto.req.connectedId.create;

import java.util.List;

import woojooin.planit.global.util.codef.dto.req.connectedId.AccountDto;

public record ConnectedIdCreateReq(List<AccountDto> accountList) {

}
