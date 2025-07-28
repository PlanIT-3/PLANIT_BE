package woojooin.planit.domain.account.isa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.member.domain.MemberProduct;

@Mapper
public interface IsaAccountMapper {

	List<MemberProduct> findAllByMemberId(@Param("memberId") Long memberId);
}