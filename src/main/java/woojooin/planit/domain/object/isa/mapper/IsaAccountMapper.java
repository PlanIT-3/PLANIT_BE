package woojooin.planit.domain.object.isa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.object.isa.dto.req.IsaAccountProductEditReq;
import woojooin.planit.domain.object.isa.dto.req.IsaAccountProductRegisterReq;
import woojooin.planit.domain.object.isa.dto.res.IsaAccountProductRes;

@Mapper
public interface IsaAccountMapper {

	List<IsaAccountProductRes> findAllByMemberId(@Param("memberId") Long memberId);

	void register(@Param("memberId") Long memberId, @Param("requestList") List<IsaAccountProductRegisterReq> isaAccountProductRegisterReqs);

	List<IsaAccountProductRes> findAllByMemberIdAndObjectId(
		@Param("memberId") Long memberId,
		@Param("objectId") Long objectId
	);

	void softDelete(@Param("memberId") Long memberId, @Param("requestList") List<IsaAccountProductEditReq> editReqs);
	void upsert(@Param("memberId") Long memberId, @Param("requestList") List<IsaAccountProductEditReq> editReqs);
}