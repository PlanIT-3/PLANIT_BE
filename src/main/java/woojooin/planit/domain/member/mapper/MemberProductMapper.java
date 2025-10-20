package woojooin.planit.domain.member.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.member.domain.MemberProduct;

public interface MemberProductMapper {

	MemberProduct findById(@Param("id") Long memberProductId);

	void insert(MemberProduct memberProduct);

	void insertBatch(List<MemberProduct> memberProducts);

	MemberProduct findByMemberProductId(@Param("memberProductId") Long memberProductId);

	List<MemberProduct> select(Long memberId);

	List<MemberProduct> findAllByMemberId(@Param("memberId") Long memberId);


}
