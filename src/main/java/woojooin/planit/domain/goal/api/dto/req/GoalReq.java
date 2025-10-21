package woojooin.planit.domain.goal.api.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import woojooin.planit.domain.goal.domain.vo.Goal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class GoalReq {
    @NotBlank(message = "목표명을 입력하세요")
    private String goalName;

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
                .goalName(goalName)
                .targetAmount(targetAmount)
                .startDate(startDate)
                .endDate(endDate)
                .depositRate(depositRate)
                .isaRate(isaRate)
                .startAmount(0L)  // 서비스에서 설정
                .goalRate(0)
                .build();
    }
}


