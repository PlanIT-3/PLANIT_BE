package woojooin.planit.domain.object.deposit.service;

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
import woojooin.planit.domain.object.deposit.dto.req.DepositProductEditListReq;
import woojooin.planit.domain.object.deposit.dto.req.DepositProductEditReq;
import woojooin.planit.domain.object.deposit.dto.req.DepositProductRegisterListReq;
import woojooin.planit.domain.object.deposit.dto.req.DepositProductRegisterReq;
import woojooin.planit.domain.object.deposit.dto.res.DepositProductRes;
import woojooin.planit.domain.object.deposit.mapper.DepositMapper;
import woojooin.planit.global.exception.BusinessException;
import woojooin.planit.global.response.ResponseCode;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class DepositService {

    private final DepositMapper depositMapper;

    /**
     * 회원의 모든 예적금 상품 조회
     * @param memberId 회원 ID
     * @return 예적금 상품 목록
     */
    public List<DepositProductRes> getMemberProductsByMemberId(Long memberId) {

        try {
            List<DepositProductRes> products = depositMapper.findAllByMemberId(memberId);
            return products;
        } catch (Exception e) {
            log.error("[DepositService.getMemberProductsByMemberId()] - failed to retrieve products memberId=\"{}\" error=\"{}\"", memberId, e.getMessage());
            throw new BusinessException(ResponseCode.DEPOSIT_PRODUCT_NOT_FOUND);
        }
    }

    /**
     * 예적금 상품 등록
     * @param memberId 회원 ID
     * @param request 등록 요청 정보
     */
    @Transactional
    public void registerMemberProductsByMemberId(Long memberId, @Valid DepositProductRegisterListReq request) {

        // 기존 검증: 요청 목록 내에서 동일한 상품 중복 등록 방지
        validateDuplicateProducts(request.getDepositProductRegisterReqs());
        
        // 🆕 추가 검증: 각 상품의 잔여액 확인하여 100% 초과 할당 방지
        validateAvailableAmount(memberId, request.getDepositProductRegisterReqs());

        try {
            depositMapper.register(memberId, request.getDepositProductRegisterReqs());
        } catch (Exception e) {
            log.error("[DepositService.registerMemberProductsByMemberId()] - failed to register products memberId=\"{}\" productCount=\"{}\" error=\"{}\"", memberId, request.getDepositProductRegisterReqs().size(), e.getMessage());
            throw new BusinessException(ResponseCode.DEPOSIT_REGISTRATION_FAILED);
        }
    }

    /**
     * 예적금 상품 수정
     * @param memberId 회원 ID
     * @param request 수정 요청 정보
     */
    @Transactional
    public void editMemberProductsByMemberId(Long memberId, @Valid DepositProductEditListReq request) {

        List<DepositProductEditReq> editReqs = request.getEditReqs();

        List<DepositProductEditReq> checkedItems = editReqs.stream()
            .filter(DepositProductEditReq::isChecked)
            .collect(Collectors.toList());

        List<DepositProductEditReq> uncheckedItems = editReqs.stream()
            .filter(req -> !req.isChecked())
            .collect(Collectors.toList());

        try {
            if (!uncheckedItems.isEmpty()) {
                depositMapper.softDelete(memberId, uncheckedItems);
            }

            if (!checkedItems.isEmpty()) {
                depositMapper.upsert(memberId, checkedItems);
            }
        } catch (Exception e) {
            log.error("[DepositService.editMemberProductsByMemberId()] - failed to edit products memberId=\"{}\" checkedCount=\"{}\" uncheckedCount=\"{}\" error=\"{}\"", memberId, checkedItems.size(), uncheckedItems.size(), e.getMessage());
            throw new BusinessException(ResponseCode.DEPOSIT_UPDATE_FAILED);
        }
    }

    /**
     * 회원의 특정 목적에 대한 예적금 상품 조회
     * @param memberId 회원 ID
     * @param objectId 목적 ID
     * @return 예적금 상품 목록
     */
    public List<DepositProductRes> findAllByMemberIdAndObjectId(Long memberId, Long objectId) {

        try {
            List<DepositProductRes> products = depositMapper.findAllByMemberIdAndObjectId(memberId, objectId);
            return products;
        } catch (Exception e) {
            log.error("[DepositService.findAllByMemberIdAndObjectId()] - failed to retrieve products memberId=\"{}\" objectId=\"{}\" error=\"{}\"", memberId, objectId, e.getMessage());
            throw new BusinessException(ResponseCode.DEPOSIT_PRODUCT_NOT_FOUND);
        }
    }

    /**
     * 중복 상품 검증
     * @param productReqs 상품 등록 요청 목록
     */
    private void validateDuplicateProducts(List<DepositProductRegisterReq> productReqs) {
        Set<Long> memberProductIds = new HashSet<>();
        for (DepositProductRegisterReq req : productReqs) {
            if (!memberProductIds.add(req.getMemberObjectId())) {
                throw new BusinessException(ResponseCode.DEPOSIT_DUPLICATE_PRODUCT);
            }
        }
    }

    /**
     * 🆕 잔여액 검증 - 요청 금액이 상품의 잔여액을 초과하지 않는지 확인
     * 
     * 검증 로직:
     * 1. 각 요청 상품에 대해 현재까지 할당된 총액을 조회
     * 2. 상품의 총 보유액에서 할당된 총액을 차감하여 잔여액 계산
     * 3. 요청 금액이 잔여액을 초과하는지 확인
     * 4. 초과하는 경우 INSUFFICIENT_AMOUNT 예외 발생
     * 
     * 이를 통해 하나의 상품을 여러 목표에 분할 할당할 수 있으면서도
     * 총 할당 금액이 상품의 보유액을 초과하지 않도록 보장
     * 
     * @param memberId 회원 ID (로깅용)
     * @param productReqs 등록 요청할 상품 목록
     * @throws BusinessException 잔여액 부족 시 INSUFFICIENT_AMOUNT 예외 발생
     */
    private void validateAvailableAmount(Long memberId, List<DepositProductRegisterReq> productReqs) {
        for (DepositProductRegisterReq req : productReqs) {
            try {
                // 1. 현재 해당 상품에 할당된 총액 조회 (NULL 방지를 위해 0으로 초기화)
                Integer currentAllocated = depositMapper.getCurrentAllocatedAmount(req.getMemberObjectId());
                if (currentAllocated == null) {
                    currentAllocated = 0;
                }
                
                // 2. 상품의 총 보유액 조회
                Integer totalAmount = depositMapper.getTotalAmount(req.getMemberObjectId());
                if (totalAmount == null) {
                    log.error("[DepositService.validateAvailableAmount()] - product not found memberProductId=\"{}\"", req.getMemberObjectId());
                    throw new BusinessException(ResponseCode.DEPOSIT_PRODUCT_NOT_FOUND);
                }
                
                // 3. 잔여액 계산 = 총 보유액 - 현재 할당된 총액
                Integer remainingAmount = totalAmount - currentAllocated;
                
                // 4. 요청 금액이 잔여액을 초과하는지 검증
                if (req.getAmount() > remainingAmount) {
                    log.warn("[DepositService.validateAvailableAmount()] - insufficient amount memberId=\"{}\" memberProductId=\"{}\" requestAmount=\"{}\" remainingAmount=\"{}\"", 
                            memberId, req.getMemberObjectId(), req.getAmount(), remainingAmount);
                    throw new BusinessException(ResponseCode.INSUFFICIENT_AMOUNT);
                }
                
                log.debug("[DepositService.validateAvailableAmount()] - validation passed memberProductId=\"{}\" totalAmount=\"{}\" currentAllocated=\"{}\" remainingAmount=\"{}\" requestAmount=\"{}\"", 
                        req.getMemberObjectId(), totalAmount, currentAllocated, remainingAmount, req.getAmount());
                        
            } catch (BusinessException e) {
                // BusinessException은 그대로 재발생
                throw e;
            } catch (Exception e) {
                log.error("[DepositService.validateAvailableAmount()] - validation failed memberProductId=\"{}\" error=\"{}\"", req.getMemberObjectId(), e.getMessage());
                throw new BusinessException(ResponseCode.DEPOSIT_VALIDATION_FAILED);
            }
        }
    }
}