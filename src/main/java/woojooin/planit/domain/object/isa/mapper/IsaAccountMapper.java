package woojooin.planit.domain.object.isa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.object.isa.dto.req.IsaAccountProductRegisterReq;
import woojooin.planit.domain.object.isa.dto.res.IsaAccountProductRes;

@Mapper
public interface IsaAccountMapper {

	List<IsaAccountProductRes> findAllByMemberId(@Param("memberId") Long memberId);

	void register(@Param("memberId") Long memberId, @Param("requestList") List<IsaAccountProductRegisterReq> isaAccountProductRegisterReqs);
}