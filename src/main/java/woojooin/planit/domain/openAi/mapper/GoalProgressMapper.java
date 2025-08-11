package woojooin.planit.domain.openAi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoalProgressMapper {
    String getRiskProfile(@Param("memberId") Long memberId);
    List<Double> getIsaProgress(@Param("memberId") Long memberId, @Param("goalId") Long goalId);
     List<Double> getDepositProgress(@Param("memberId") Long memberId, @Param("goalId") Long goalId);
}
