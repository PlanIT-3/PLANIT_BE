package woojooin.planit.domain.member.service;


import woojooin.planit.domain.member.domain.Member;

public interface MemberService {
    Member findById(Long memberId);
    Member findByEmail(String email);
    void save(Member member);
    void update(Member member);
    void updateInvestType(Long memberId, String type);
}