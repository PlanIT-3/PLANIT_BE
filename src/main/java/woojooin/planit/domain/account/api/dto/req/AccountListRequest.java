package woojooin.planit.domain.account.api.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.global.util.codef.dto.connectedId.AccountDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountListRequest {
    private AccountDto accountDto;
    private boolean last;
}