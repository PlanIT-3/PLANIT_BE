package woojooin.planit.global.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import woojooin.planit.domain.member.domain.Member;
import woojooin.planit.domain.member.repository.MemberRepository;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByEmail(username);
        if (member == null)
            throw new UsernameNotFoundException("User not found");
        return new CustomUserDetails(member);
    }

    // 커스텀 메서드 추가 ✅
    public UserDetails loadUserByMemberId(Long memberId) throws UsernameNotFoundException {
        log.debug("Finding user by memberId: {}", memberId);

        Member member = memberRepository.findById(memberId);
        if (member == null) {
            log.error("User not found with memberId: {}", memberId);
            throw new UsernameNotFoundException("User not found with memberId: " + memberId);
        }

        log.debug("User found: {} (ID: {})", member.getMemberId(), member.getMemberId());
        return new CustomUserDetails(member);
    }
}