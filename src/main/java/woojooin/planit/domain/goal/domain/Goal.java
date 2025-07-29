package woojooin.planit.domain.goal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate; // DATE 타입 매핑
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {
    private Long goalId;       // `object_id` 컬럼 매핑
    private Long userId;         // `user_id` 컬럼 매핑 (member 테이블의 member_id 참조)
    private String goalName;   // `object_name` 컬럼 매핑
    private Long goalAmount;   // `object_amount` 컬럼 매핑
    private LocalDate startDate; // `start_date` 컬럼 매핑
    private LocalDate endDate;   // `end_date` 컬럼 매핑
    private Integer depositRatio; // `deposit_ratio` 컬럼 매핑
    private Integer isaRatio;     // `isa_ratio` 컬럼 매핑
}
