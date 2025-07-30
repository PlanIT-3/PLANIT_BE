package woojooin.planit.domain.object.isa.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import woojooin.planit.domain.object.isa.dto.req.IsaAccountProductEditListReq;
import woojooin.planit.domain.object.isa.dto.req.IsaAccountProductEditReq;
import woojooin.planit.domain.object.isa.dto.req.IsaAccountProductRegisterListReq;
import woojooin.planit.domain.object.isa.dto.res.IsaAccountProductRes;
import woojooin.planit.domain.object.isa.mapper.IsaAccountMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IsaAccountService {
    
    private final IsaAccountMapper isaAccountMapper;
    
    public List<IsaAccountProductRes> getMemberProductsByMemberId(Long memberId) {

        List<IsaAccountProductRes> products = isaAccountMapper.findAllByMemberId(memberId);
        
        return products;
    }

    @Transactional
    public void registerMemberProductsByMemberId(Long memberId, IsaAccountProductRegisterListReq isaAccountProductRegisterListReq) {

        isaAccountMapper.register(memberId, isaAccountProductRegisterListReq.getIsaAccountProductRegisterReqs());
    }

    @Transactional
    public void editMemberProductsByMemberId(Long memberId, IsaAccountProductEditListReq request) {
        List<IsaAccountProductEditReq> editReqs = request.getEditReqs();

        if (editReqs == null || editReqs.isEmpty()) {
            return;
        }

        List<IsaAccountProductEditReq> checkedItems = editReqs.stream()
            .filter(IsaAccountProductEditReq::isChecked)
            .collect(Collectors.toList());

        List<IsaAccountProductEditReq> uncheckedItems = editReqs.stream()
            .filter(req -> !req.isChecked())
            .collect(Collectors.toList());

        if (!uncheckedItems.isEmpty()) {
            isaAccountMapper.softDelete(memberId, uncheckedItems);
        }

        if (!checkedItems.isEmpty()) {
            isaAccountMapper.upsert(memberId, checkedItems);
        }
    }

    public List<IsaAccountProductRes> findAllByMemberIdAndObjectId(Long memberId, Long objectId) {
        return isaAccountMapper.findAllByMemberIdAndObjectId(memberId, objectId);
    }
}