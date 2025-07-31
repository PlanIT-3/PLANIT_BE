package woojooin.planit.domain.object.deposit.dto.res;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.product.domain.ProductTypeCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositProductRes {

	private Long memberProductId;

	private ProductTypeCode productTypeCode;

	private BigDecimal presentAmount;

	private Integer quantity;

	private String itemName;

	private String itemCode;

	private boolean checked;

	private Integer allocatedAmount;  // 현재 할당된 총액
	
	private Integer remainingAmount;  // 할당 가능한 잔여액
}