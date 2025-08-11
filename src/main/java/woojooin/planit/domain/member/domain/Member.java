package woojooin.planit.domain.member.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Member {
    private Long memberId;
    private String role;
    private String connectedId;
    private Integer rewardCnt;
    private String socialId;
    private String authVender;
    private String investType;
    private LocalDateTime lastVisit;
    private String email;
    private String password;
    private Boolean benefit;
    private String nickname;
    private Boolean isAgreed;
    private Double stable;
    private Double income;
    private Double liquid;
    private Double growth;
    private Double diversified;
}
