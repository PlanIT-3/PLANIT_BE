package woojooin.planit.domain.member.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import woojooin.planit.domain.member.domain.Member;
import woojooin.planit.domain.member.mapper.MemberMapper;

@Repository
public class MemberRepository {

    private final MemberMapper memberMapper;

    public MemberRepository(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public Member findById(Long memberId) {
        return memberMapper.findById(memberId);
    }

    public void save(Member member) {
        memberMapper.insert(member);
    }

    public void update(Member member) {
        memberMapper.update(member);
    }

    public void delete(Long memberId) {
        memberMapper.delete(memberId);
    }

    public Member findByEmail(String email) {
        return memberMapper.findByEmail(email);
    }
}
