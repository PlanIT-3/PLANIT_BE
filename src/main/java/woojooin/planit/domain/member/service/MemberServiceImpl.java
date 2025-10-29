package woojooin.planit.domain.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woojooin.planit.domain.member.domain.Member;
import woojooin.planit.domain.member.api.dto.req.RealInvestTypeReq;
import woojooin.planit.domain.member.api.dto.res.InvestScoreRes;
import woojooin.planit.domain.member.mapper.MemberMapper;
import woojooin.planit.domain.member.repository.MemberRepository;
import woojooin.planit.domain.openAi.dto.req.DefaultInvestTypeReq;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }

    @Override
    public void update(Member member) {
        memberRepository.update(member);
    }
    @Override
    public void updateInvestType(Long memberId, String type) {
        memberRepository.updateInvestType(memberId, type);
    }

    @Override
    public InvestScoreRes getInvestScore(Long id) {
        Member member = memberRepository.findById(id);
        if (member == null) {
            return null;
        }

        DefaultInvestTypeReq surveyInvestmentType = DefaultInvestTypeReq.getInvestType(member.getInvestType());
        
        RealInvestTypeReq realInvestType = RealInvestTypeReq.fromScores(
            member.getStable(),
            member.getIncome(),
            member.getLiquid(),
            member.getGrowth(),
            member.getDiversified()
        );

        return new InvestScoreRes(id, surveyInvestmentType, realInvestType);
    }


    @Override
    public String getInvestmentType(Long memberId) {
        return memberRepository.findInvestTypeById(memberId);
    }

}
