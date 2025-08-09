package woojooin.planit.domain.goal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.goal.deposit.dto.res.DepositAccountRes;
import woojooin.planit.domain.goal.isa.dto.res.IsaAccountProductRes;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalDetailResponseDto {
    private String goalName;
    private Long targetAmount;
    private Long totalAmount;
    private Integer goalRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private int depositRate;
    private int isaRate;
    private List<IsaAccountProductRes> isaProducts; // 변수명 통일 (isaAccounts -> isaProducts)
    private List<DepositAccountRes> depositAccounts; // 예적금 목록 추가
}
