package woojooin.planit.domain.goal.api.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.goal.domain.enums.Bank;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalDepositRes {
    private String goalName;
    private Long accountId;
    private String accountName;
    BigDecimal accountBalance;
    private String bankCode;

    // 은행 한글명을 반환하는 메서드
    public String getBankName() {
        return Bank.getNameByCode(this.bankCode);
    }
}
