package woojooin.planit.global.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import woojooin.planit.domain.member.domain.Member;
import woojooin.planit.domain.member.repository.MemberRepository;

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
}