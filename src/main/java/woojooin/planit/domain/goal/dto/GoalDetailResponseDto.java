package woojooin.planit.domain.goal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.object.deposit.dto.res.DepositAccountRes;
import woojooin.planit.domain.object.isa.dto.res.IsaAccountProductRes;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoalDetailResponseDto {
    private String objectName;
    private Long targetAmount;
    private Long totalAmount;
    private Integer goalRate;
    private LocalDate endDate;
    private List<IsaAccountProductRes> isaProducts; // 변수명 통일 (isaAccounts -> isaProducts)
    private List<DepositAccountRes> depositAccounts; // 예적금 목록 추가
}
