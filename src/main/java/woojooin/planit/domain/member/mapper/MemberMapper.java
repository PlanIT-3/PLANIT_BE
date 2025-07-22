package woojooin.planit.domain.member.mapper;

import woojooin.planit.domain.member.domain.Member;

public interface MemberMapper {
    Member findById(Long memberId);
    Member findByEmail(String email);
    void insert(Member member);
    void update(Member member);
    void delete(Long memberId);
}