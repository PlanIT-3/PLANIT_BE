package woojooin.planit.domain.member.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.member.domain.MemberProduct;

public interface MemberProductMapper {
	void insert(MemberProduct memberProduct);

	MemberProduct findByMemberId(@Param("memberProductId") Long memberProductId);

	List<MemberProduct> select(Long memberId);
}
