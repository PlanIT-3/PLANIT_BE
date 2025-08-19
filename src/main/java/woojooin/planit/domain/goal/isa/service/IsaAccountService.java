package woojooin.planit.domain.goal.isa.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.account.mapper.AccountMapper;
import woojooin.planit.domain.goal.isa.dto.req.IsaAccountProductEditListReq;
import woojooin.planit.domain.goal.isa.dto.req.IsaAccountProductEditReq;
import woojooin.planit.domain.goal.isa.dto.req.IsaAccountProductRegisterListReq;
import woojooin.planit.domain.goal.isa.dto.req.IsaAccountProductRegisterReq;
import woojooin.planit.domain.goal.isa.dto.res.IsaAccountProductRes;
import woojooin.planit.domain.goal.isa.dto.res.IsaAccountTaxExemptionRes;
import woojooin.planit.domain.goal.isa.mapper.IsaAccountMapper;
import woojooin.planit.global.exception.BusinessException;
import woojooin.planit.global.response.ResponseCode;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IsaAccountService {

    private final IsaAccountMapper isaAccountMapper;
    private final AccountMapper accountMapper;

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
            log.info("memberId:{}", memberId);
            Long accountId = accountMapper.findIsaAccountIdByMemberId(memberId);
            log.info("accountId:{}", accountId);
            isaAccountMapper.register(memberId, accountId, request.getIsaAccountProductRegisterReqs());
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
                log.info("memberId:{}",memberId);
                Long accountId = accountMapper.findIsaAccountIdByMemberId(memberId);
                log.info("fasfas");
                log.info("accountId:{}", accountId);
                isaAccountMapper.upsert(memberId, accountId, checkedItems);
            }
        } catch (Exception e) {
            log.error("[IsaAccountService.editMemberProductsByMemberId()] - failed to edit products memberId=\"{}\" checkedCount=\"{}\" uncheckedCount=\"{}\" error=\"{}\"", memberId, checkedItems.size(), uncheckedItems.size(), e.getMessage());
            throw new BusinessException(ResponseCode.ISA_UPDATE_FAILED);
        }
    }

    public List<IsaAccountProductRes> findAllByMemberIdAndGoalId(Long memberId, Long goalId) {

        try {
            List<IsaAccountProductRes> products = isaAccountMapper.findAllByMemberIdAndGoalId(memberId, goalId);
            return products;
        } catch (Exception e) {
            log.error("[IsaAccountService.findAllByMemberIdAndGoalId()] - failed to retrieve products memberId=\"{}\" objectId=\"{}\" error=\"{}\"", memberId, goalId, e.getMessage());
            throw new BusinessException(ResponseCode.ISA_PRODUCT_NOT_FOUND);
        }
    }

    private void validateDuplicateProducts(List<IsaAccountProductRegisterReq> productReqs) {
        Set<Long> memberProductIds = new HashSet<>();
        for (IsaAccountProductRegisterReq req : productReqs) {
            if (!memberProductIds.add(req.getMemberProductId())) {
                throw new BusinessException(ResponseCode.ISA_DUPLICATE_PRODUCT);
            }
        }
    }

    public IsaAccountTaxExemptionRes getIsaAccountTaxExemptionByMemberId(Long memberId) {
        try {
            Long isaProfit = isaAccountMapper.getTotalIsaProfitByMemberId(memberId);
            
            long taxSavedAmount = 0L;
            if (isaProfit != null && isaProfit > 2000000L) {
                long excessAmount = isaProfit - 2000000L;
                taxSavedAmount = (long) (excessAmount * 0.099);
            }
            
            return new IsaAccountTaxExemptionRes(taxSavedAmount);
            
        } catch (Exception e) {
            log.error("[IsaAccountService.getIsaAccountTaxExemptionByMemberId()] - failed to calculate tax exemption memberId=\"{}\" error=\"{}\"", memberId, e.getMessage());
            throw new BusinessException(ResponseCode.ISA_TAX_CALCULATION_FAILED);
        }
    }
}