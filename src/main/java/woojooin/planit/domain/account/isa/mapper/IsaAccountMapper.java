package woojooin.planit.domain.account.isa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.account.isa.dto.res.IsaAccountProductRes;

@Mapper
public interface IsaAccountMapper {

	List<IsaAccountProductRes> findAllByMemberId(@Param("memberId") Long memberId);
}