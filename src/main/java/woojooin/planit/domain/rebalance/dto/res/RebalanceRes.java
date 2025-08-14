package woojooin.planit.domain.rebalance.dto.res;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RebalanceRes {
	List<RebalancingInfo> rebalancingInfo;
}
