package woojooin.planit.domain.account.isa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import woojooin.planit.domain.account.isa.dto.res.IsaAccountProductRes;
import woojooin.planit.domain.account.isa.mapper.IsaAccountMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IsaAccountService {
    
    private final IsaAccountMapper isaAccountMapper;
    
    public List<IsaAccountProductRes> getMemberProductsByMemberId(Long memberId) {

        List<IsaAccountProductRes> products = isaAccountMapper.findAllByMemberId(memberId);
        
        return products;
    }
}