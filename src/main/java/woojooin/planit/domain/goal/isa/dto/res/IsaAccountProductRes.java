package woojooin.planit.domain.goal.isa.dto.res;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsaAccountProductRes {

	private Long memberProductId;
	private BigDecimal presentAmount;
	private Integer quantity;
	private String itemName;
	private boolean checked;
}
