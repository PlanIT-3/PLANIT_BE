package woojooin.planit.domain.goal.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import woojooin.planit.domain.goal.domain.Goal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class GoalRequestDto {
    @NotBlank(message = "목표명을 입력하세요")
    private String objectName;

    @NotNull
    @Min(1)
    private Long targetAmount;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @Min(0) @Max(100)
    private int depositRate;

    @Min(0) @Max(100)
    private int isaRate;

    public Goal toEntity() {
        return Goal.builder()
                .goalName(objectName)
                .targetAmount(targetAmount)
                .startDate(startDate)
                .endDate(endDate)
                .depositRate(depositRate)
                .isaRate(isaRate)
                .startAmount(null)  // 서비스에서 설정
                .goalRate(null)
                .build();
    }
}


