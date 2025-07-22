package woojooin.planit.domain.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}