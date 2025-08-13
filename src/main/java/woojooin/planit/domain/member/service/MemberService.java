package woojooin.planit.domain.member.service;


import woojooin.planit.domain.member.domain.Member;
import woojooin.planit.domain.member.dto.res.InvestScoreRes;

public interface MemberService {
    Member findById(Long memberId);
    Member findByEmail(String email);
    void save(Member member);
    void update(Member member);
    void updateInvestType(Long memberId, String type);

	InvestScoreRes getInvestScore(Long id);

    String getInvestmentType(Long memberId);
}