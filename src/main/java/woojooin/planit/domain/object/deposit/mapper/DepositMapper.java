package woojooin.planit.domain.object.deposit.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.object.deposit.dto.req.DepositProductEditReq;
import woojooin.planit.domain.object.deposit.dto.req.DepositProductRegisterReq;
import woojooin.planit.domain.object.deposit.dto.res.DepositProductRes;

@Mapper
public interface DepositMapper {

	/**
	 * 회원의 모든 예적금 상품 조회
	 * @param memberId 회원 ID
	 * @return 예적금 상품 목록
	 */
	List<DepositProductRes> findAllByMemberId(@Param("memberId") Long memberId);

	/**
	 * 회원의 특정 목적에 대한 예적금 상품 조회
	 * @param memberId 회원 ID
	 * @param objectId 목적 ID
	 * @return 예적금 상품 목록
	 */
	List<DepositProductRes> findAllByMemberIdAndObjectId(@Param("memberId") Long memberId, @Param("objectId") Long objectId);

	/**
	 * 예적금 상품 등록
	 * @param memberId 회원 ID
	 * @param requestList 등록할 예적금 상품 목록
	 */
	void register(@Param("memberId") Long memberId, @Param("requestList") List<DepositProductRegisterReq> requestList);

	/**
	 * 예적금 상품 소프트 삭제
	 * @param memberId 회원 ID
	 * @param requestList 삭제할 예적금 상품 목록
	 */
	void softDelete(@Param("memberId") Long memberId, @Param("requestList") List<DepositProductEditReq> requestList);

	/**
	 * 예적금 상품 업서트 (존재하면 업데이트, 없으면 삽입)
	 * @param memberId 회원 ID
	 * @param requestList 업서트할 예적금 상품 목록
	 */
	void upsert(@Param("memberId") Long memberId, @Param("requestList") List<DepositProductEditReq> requestList);

	/**
	 * 특정 상품의 현재 할당된 총액 조회
	 * - 목적: 예적금 상품 등록 시 잔여액 검증을 위해 사용
	 * - 계산 방식: action 테이블에서 해당 상품에 할당된 모든 금액의 합계
	 * - 반환값: 할당된 총액 (할당 기록이 없으면 0 반환)
	 * @param memberProductId 회원 상품 ID
	 * @return 현재까지 할당된 총 금액
	 */
	Integer getCurrentAllocatedAmount(@Param("memberProductId") Long memberProductId);

	/**
	 * 특정 상품의 총 보유액 조회
	 * - 목적: 예적금 상품 등록 시 잔여액 검증을 위해 사용
	 * - 반환값: member_product 테이블의 present_amount 값
	 * @param memberProductId 회원 상품 ID
	 * @return 상품의 총 보유액
	 */
	Integer getTotalAmount(@Param("memberProductId") Long memberProductId);
}