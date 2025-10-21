package woojooin.planit.domain.goal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import woojooin.planit.domain.goal.api.dto.req.IsaAccountProductEditReq;
import woojooin.planit.domain.goal.api.dto.req.IsaAccountProductRegisterReq;
import woojooin.planit.domain.goal.api.dto.res.IsaAccountProductRes;

@Mapper
public interface IsaAccountMapper {

	List<IsaAccountProductRes> findAllByMemberId(@Param("memberId") Long memberId);

	void register(@Param("memberId") Long memberId, @Param("accountId") Long accountId, @Param("requestList") List<IsaAccountProductRegisterReq> isaAccountProductRegisterReqs);

	List<IsaAccountProductRes> findAllByMemberIdAndGoalId(
		@Param("memberId") Long memberId,
		@Param("goalId") Long goalId
	);

	void softDelete(@Param("memberId") Long memberId, @Param("requestList") List<IsaAccountProductEditReq> editReqs);

	void upsert(@Param("memberId") Long memberId, @Param("accountId") Long accountId, @Param("requestList") List<IsaAccountProductEditReq> editReqs);

	Long getTotalIsaProfitByMemberId(@Param("memberId") Long memberId);
}