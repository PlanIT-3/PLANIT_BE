package woojooin.planit.domain.goal.api.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalDetailRes {
    private Long goalId;
    private String goalName;
    private Long targetAmount;
    private Long totalAmount;
    private Integer goalRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private int depositRate;
    private int isaRate;
    private List<IsaAccountProductRes> isaProducts; // 변수명 통일 (isaAccounts -> isaProducts)
    private List<GoalDepositAmountRes> depositAccounts; // 예적금 목록 추가
}
