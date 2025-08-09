package woojooin.planit.domain.goal.goalAccount.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import woojooin.planit.domain.goal.goalAccount.dto.res.GoalDepositRes;
import woojooin.planit.domain.goal.goalAccount.dto.res.GoalIsaRes;

import java.util.List;

@Mapper
public interface GoalAccountMapper {
    List<GoalIsaRes> findIsaByMemberId(@Param("memberId") Long memberId, @Param("goalId") Long goalId);

    List<GoalDepositRes> findDepositByMemberId(@Param("memberId") Long memberId, @Param("goalId") Long goalId);
}
