package woojooin.planit.domain.member.mapper;

import org.apache.ibatis.annotations.Param;
import woojooin.planit.domain.member.domain.Member;

public interface MemberMapper {
    Member findById(Long memberId);
    Member findByEmail(String email);
    void insert(Member member);
    void update(Member member);
    void delete(Long memberId);

    void updateInvestType(@Param("memberId") Long memberId, @Param("type") String type);
}