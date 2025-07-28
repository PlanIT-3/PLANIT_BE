package woojooin.planit.domain.account.isa.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.product.domain.ProductTypeCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsaAccountProductDto {

	private Long memberProductId;
	private ProductTypeCode productTypeCode;
	private BigDecimal presentAmount;
	private Integer quantity;
	private String itemName;
	private String itemCode;
}
