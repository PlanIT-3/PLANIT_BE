package woojooin.planit.domain.account.isa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import woojooin.planit.domain.account.isa.mapper.IsaAccountMapper;
import woojooin.planit.domain.member.domain.MemberProduct;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IsaAccountService {
    
    private final IsaAccountMapper isaAccountMapper;
    
    public List<MemberProduct> getMemberProductsByMemberId(Long memberId) {

        List<MemberProduct> products = isaAccountMapper.findAllByMemberId(memberId);
        
        return products;
    }
}