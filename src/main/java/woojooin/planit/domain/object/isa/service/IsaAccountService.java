package woojooin.planit.domain.object.isa.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.object.isa.dto.req.IsaAccountProductEditListReq;
import woojooin.planit.domain.object.isa.dto.req.IsaAccountProductEditReq;
import woojooin.planit.domain.object.isa.dto.req.IsaAccountProductRegisterListReq;
import woojooin.planit.domain.object.isa.dto.req.IsaAccountProductRegisterReq;
import woojooin.planit.domain.object.isa.dto.res.IsaAccountProductRes;
import woojooin.planit.domain.object.isa.mapper.IsaAccountMapper;
import woojooin.planit.global.exception.BusinessException;
import woojooin.planit.global.response.ResponseCode;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IsaAccountService {

    private final IsaAccountMapper isaAccountMapper;

    public List<IsaAccountProductRes> getMemberProductsByMemberId(Long memberId) {

        try {
            List<IsaAccountProductRes> products = isaAccountMapper.findAllByMemberId(memberId);
            return products;
        } catch (Exception e) {
            log.error("[IsaAccountService.getMemberProductsByMemberId()] - failed to retrieve products memberId=\"{}\" error=\"{}\"", memberId, e.getMessage());
            throw new BusinessException(ResponseCode.ISA_PRODUCT_NOT_FOUND);
        }
    }

    @Transactional
    public void registerMemberProductsByMemberId(Long memberId, @Valid IsaAccountProductRegisterListReq request) {

        validateDuplicateProducts(request.getIsaAccountProductRegisterReqs());

        try {
            isaAccountMapper.register(memberId, request.getIsaAccountProductRegisterReqs());
        } catch (Exception e) {
            log.error("[IsaAccountService.registerMemberProductsByMemberId()] - failed to register products memberId=\"{}\" productCount=\"{}\" error=\"{}\"", memberId, request.getIsaAccountProductRegisterReqs().size(), e.getMessage());
            throw new BusinessException(ResponseCode.ISA_REGISTRATION_FAILED);
        }
    }

    @Transactional
    public void editMemberProductsByMemberId(Long memberId, @Valid IsaAccountProductEditListReq request) {

        List<IsaAccountProductEditReq> editReqs = request.getEditReqs();

        List<IsaAccountProductEditReq> checkedItems = editReqs.stream()
            .filter(IsaAccountProductEditReq::isChecked)
            .collect(Collectors.toList());

        List<IsaAccountProductEditReq> uncheckedItems = editReqs.stream()
            .filter(req -> !req.isChecked())
            .collect(Collectors.toList());

        try {
            if (!uncheckedItems.isEmpty()) {
                isaAccountMapper.softDelete(memberId, uncheckedItems);
            }

            if (!checkedItems.isEmpty()) {
                isaAccountMapper.upsert(memberId, checkedItems);
            }
        } catch (Exception e) {
            log.error("[IsaAccountService.editMemberProductsByMemberId()] - failed to edit products memberId=\"{}\" checkedCount=\"{}\" uncheckedCount=\"{}\" error=\"{}\"", memberId, checkedItems.size(), uncheckedItems.size(), e.getMessage());
            throw new BusinessException(ResponseCode.ISA_UPDATE_FAILED);
        }
    }

    public List<IsaAccountProductRes> findAllByMemberIdAndObjectId(Long memberId, Long objectId) {

        try {
            List<IsaAccountProductRes> products = isaAccountMapper.findAllByMemberIdAndObjectId(memberId, objectId);
            return products;
        } catch (Exception e) {
            log.error("[IsaAccountService.findAllByMemberIdAndObjectId()] - failed to retrieve products memberId=\"{}\" objectId=\"{}\" error=\"{}\"", memberId, objectId, e.getMessage());
            throw new BusinessException(ResponseCode.ISA_PRODUCT_NOT_FOUND);
        }
    }

    private void validateDuplicateProducts(List<IsaAccountProductRegisterReq> productReqs) {
        Set<Long> memberProductIds = new HashSet<>();
        for (IsaAccountProductRegisterReq req : productReqs) {
            if (!memberProductIds.add(req.getMemberObjectId())) {
                throw new BusinessException(ResponseCode.ISA_DUPLICATE_PRODUCT);
            }
        }
    }
}