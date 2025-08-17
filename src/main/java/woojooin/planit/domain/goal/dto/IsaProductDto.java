package woojooin.planit.domain.goal.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsaProductDto {
	private BigDecimal presentAmount; // 현재가
	private BigDecimal quantity;      // 보유 수량
}
