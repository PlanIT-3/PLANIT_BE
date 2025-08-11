package woojooin.planit.domain.goal.goalAccount.dto.res;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalIsaRes {
    private Long memberId; // goal.memberId
    private String goalName; // goal
    private Long memberProductId; // member_product
    private String itemName; // member_product.itemName
    private BigDecimal presentAmount; // member_product.presentAmount
    private Integer quantity; // member_product.quantity

    public BigDecimal getIsaBalance() {
        return presentAmount.multiply(BigDecimal.valueOf(quantity));
    }
}


