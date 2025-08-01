package woojooin.planit.domain.object.deposit.service;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import woojooin.planit.domain.object.deposit.dto.req.DepositAccountEditListReq;
import woojooin.planit.domain.object.deposit.dto.req.DepositAccountEditReq;
import woojooin.planit.domain.object.deposit.dto.req.DepositAccountRegisterListReq;
import woojooin.planit.domain.object.deposit.dto.req.DepositAccountRegisterReq;
import woojooin.planit.domain.object.deposit.dto.res.DepositAccountRes;
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
     * 회원의 예적금 계좌 조회
     * @param memberId 회원 ID
     * @return 예적금 계좌 목록
     */
    public List<DepositAccountRes> getMemberAccountsByMemberId(Long memberId) {

        try {
            List<DepositAccountRes> accounts = depositMapper.findAllByMemberId(memberId);
            return accounts;
        } catch (Exception e) {
            log.error("[DepositService.getMemberAccountsByMemberId()] - failed to retrieve accounts memberId=\"{}\" error=\"{}\"", memberId, e.getMessage());
            throw new BusinessException(ResponseCode.DEPOSIT_ACCOUNT_NOT_FOUND);
        }
    }

    /**
     * 예적금 계좌 등록
     * @param memberId 회원 ID
     * @param request 등록 요청 정보
     */
    @Transactional
    public void registerMemberAccountsByMemberId(Long memberId, @Valid DepositAccountRegisterListReq request) {

        // 기존 검증: 요청 목록 내에서 동일한 계좌 중복 등록 방지
        validateDuplicateAccounts(request.getDepositAccountRegisterReqs());
        
        // 추가 검증: 각 계좌의 잔여액 확인하여 100% 초과 할당 방지
        validateAvailableAmount(memberId, request.getDepositAccountRegisterReqs());

        try {
            depositMapper.register(memberId, request.getDepositAccountRegisterReqs());
        } catch (Exception e) {
            log.error("[DepositService.registerMemberAccountsByMemberId()] - failed to register accounts memberId=\"{}\" accountCount=\"{}\" error=\"{}\"", memberId, request.getDepositAccountRegisterReqs().size(), e.getMessage());
            throw new BusinessException(ResponseCode.DEPOSIT_REGISTRATION_FAILED);
        }
    }

    /**
     * 예적금 수정
     * @param memberId 회원 ID
     * @param request 수정 요청 정보
     */
    @Transactional
    public void editMemberAccountsByMemberId(Long memberId, @Valid DepositAccountEditListReq request) {

        List<DepositAccountEditReq> editReqs = request.getEditReqs();

        List<DepositAccountEditReq> checkedItems = editReqs.stream()
            .filter(DepositAccountEditReq::isChecked)
            .collect(Collectors.toList());

        List<DepositAccountEditReq> uncheckedItems = editReqs.stream()
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
            log.error("[DepositService.editMemberAccountsByMemberId()] - failed to edit accounts memberId=\"{}\" checkedCount=\"{}\" uncheckedCount=\"{}\" error=\"{}\"", memberId, checkedItems.size(), uncheckedItems.size(), e.getMessage());
            throw new BusinessException(ResponseCode.DEPOSIT_UPDATE_FAILED);
        }
    }

    /**
     * 회원의 특정 목적에 대한 예적금 조회
     * @param memberId 회원 ID
     * @param objectId 목적 ID
     * @return 예적금 목록
     */
    public List<DepositAccountRes> findAllByMemberIdAndObjectId(Long memberId, Long objectId) {

        try {
            List<DepositAccountRes> accounts = depositMapper.findAllByMemberIdAndObjectId(memberId, objectId);
            return accounts;
        } catch (Exception e) {
            log.error("[DepositService.findAllByMemberIdAndObjectId()] - failed to retrieve accounts memberId=\"{}\" objectId=\"{}\" error=\"{}\"", memberId, objectId, e.getMessage());
            throw new BusinessException(ResponseCode.DEPOSIT_ACCOUNT_NOT_FOUND);
        }
    }

    /**
     * 회원의 특정 목적에 할당 가능한 예적금 조회
     * @param memberId 회원 ID
     * @param objectId 목적 ID
     * @return 할당 가능한 예적금 목록
     */
    public List<DepositAccountRes> findAvailableAccountsByMemberIdAndObjectId(Long memberId, Long objectId) {

        try {
            List<DepositAccountRes> accounts = depositMapper.findAvailableAccountsByMemberIdAndObjectId(memberId, objectId);
            return accounts;
        } catch (Exception e) {
            log.error("[DepositService.findAvailableAccountsByMemberIdAndObjectId()] - failed to retrieve available accounts memberId=\"{}\" objectId=\"{}\" error=\"{}\"", memberId, objectId, e.getMessage());
            throw new BusinessException(ResponseCode.DEPOSIT_ACCOUNT_NOT_FOUND);
        }
    }

    /**
     * 중복 검증
     * @param accountReqs 등록 요청 목록
     */
    private void validateDuplicateAccounts(List<DepositAccountRegisterReq> accountReqs) {
        Set<Long> memberAccountIds = new HashSet<>();
        for (DepositAccountRegisterReq req : accountReqs) {
            if (!memberAccountIds.add(req.getMemberAccountId())) {
                throw new BusinessException(ResponseCode.DEPOSIT_DUPLICATE_ACCOUNT);
            }
        }
    }

    /**
     * 요청 금액이 계좌의 잔여액을 초과하지 않는지 확인
     * 
     *  각 요청 계좌에 대해 현재까지 할당된 총액을 조회
     *  총 보유액에서 할당된 총액을 차감한 잔여액과 요청 금액을 비교
     *  요청 금액이 잔여액을 초과하면 예외 발생
     * 
     * @param memberId 회원 ID
     * @param accountReqs 등록 요청 목록
     */
    private void validateAvailableAmount(Long memberId, List<DepositAccountRegisterReq> accountReqs) {
        for (DepositAccountRegisterReq req : accountReqs) {
            // 현재 할당된 총액 조회
            Integer currentAllocatedAmount = depositMapper.getCurrentAllocatedAmount(req.getMemberAccountId());
            
            // 총 보유액 조회
            Integer totalAmount = depositMapper.getTotalAmount(req.getMemberAccountId());
            
            if (totalAmount == null) {
                log.error("[DepositService.validateAvailableAmount()] - total amount is null memberAccountId=\"{}\"", req.getMemberAccountId());
                throw new BusinessException(ResponseCode.DEPOSIT_ACCOUNT_NOT_FOUND);
            }
            
            // 잔여액 계산 = 총 보유액 - 현재 할당된 총액
            int remainingAmount = totalAmount - currentAllocatedAmount;
            
            // 요청 금액이 잔여액을 초과하는지 확인
            if (req.getAmount() > remainingAmount) {
                log.error("[DepositService.validateAvailableAmount()] - insufficient remaining amount memberAccountId=\"{}\" requestedAmount=\"{}\" remainingAmount=\"{}\"", req.getMemberAccountId(), req.getAmount(), remainingAmount);
                throw new BusinessException(ResponseCode.DEPOSIT_INSUFFICIENT_AMOUNT);
            }
        }
    }
}