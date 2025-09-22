package woojooin.planit.domain.goal.domain.vo;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Goal {
    private Long goalId;
    private Long memberId;

    private String goalName;
    private Long targetAmount;

    private LocalDate startDate;
    private LocalDate endDate;

    private Integer depositRate;
    private Integer isaRate;

    private Long startAmount;
    private Integer goalRate;      // 목표 달성률
}
