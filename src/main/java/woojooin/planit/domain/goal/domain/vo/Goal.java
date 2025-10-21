package woojooin.planit.domain.goal.domain.vo;

import lombok.*;
import woojooin.planit.domain.member.domain.Member;
import woojooin.planit.domain.rebalance.vo.Rebalance;

import java.time.LocalDate;
import java.util.List;

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

    private Member member;
    private List<Rebalance> rebalances;
    private List<Action> actions;
}
