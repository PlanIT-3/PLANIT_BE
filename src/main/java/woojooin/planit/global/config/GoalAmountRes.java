package woojooin.planit.global.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalAmountRes {
    BigDecimal targetAmount; // 목표 금액
}
