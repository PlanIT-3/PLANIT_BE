package woojooin.planit.domain.account.api.dto.res;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BalanceListRes {
	private List<BalanceRes> balanceResList;
}
