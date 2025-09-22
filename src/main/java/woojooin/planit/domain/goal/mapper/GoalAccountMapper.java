package woojooin.planit.domain.goal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import woojooin.planit.domain.goal.api.dto.res.GoalDepositAmountRes;
import woojooin.planit.domain.goal.api.dto.res.GoalIsaRes;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface GoalAccountMapper {
    List<GoalIsaRes> findIsaByMemberId(@Param("memberId") Long memberId, @Param("goalId") Long goalId);

    List<GoalDepositAmountRes> findDepositByMemberId(@Param("memberId") Long memberId, @Param("goalId") Long goalId);

    BigDecimal getTargetAmount(@Param("memberId") Long memberId, @Param("goalId") Long goalId);
}
