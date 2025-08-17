package woojooin.planit.domain.member.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import woojooin.planit.domain.member.dto.req.RealInvestTypeReq;
import woojooin.planit.domain.openAi.dto.req.DefaultInvestTypeReq;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvestScoreRes {
    private Long memberId;
    private DefaultInvestTypeReq surveyInvestmentType;
    private RealInvestTypeReq realInvestType;
}