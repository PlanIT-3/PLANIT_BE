package woojooin.planit.domain.goal.domain;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Goal {
    private Long objectId;
    private Long memberId;

    private String objectName;
    private Long targetAmount;

    private LocalDate startDate;
    private LocalDate endDate;

    private Integer depositRate;
    private Integer isaRate;

    private Long startAmount;
    private Integer goalRate;      // 목표 달성률


}
