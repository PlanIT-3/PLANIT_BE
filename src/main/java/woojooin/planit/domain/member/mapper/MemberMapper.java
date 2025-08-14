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

    String findByConnectedIdString(@Param("memberId") Long memberId);
    
    void updateConnectedId(@Param("memberId") Long memberId, @Param("connectedId") String connectedId);
    String findInvestTypeById(@Param("memberId") Long memberId);
    
    void updateIsaType(@Param("memberId") Long memberId, @Param("isaType") String isaType);
}