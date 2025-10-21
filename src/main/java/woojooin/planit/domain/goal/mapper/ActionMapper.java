package woojooin.planit.domain.goal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import woojooin.planit.domain.goal.domain.vo.Action;

@Mapper
public interface ActionMapper {
	List<Action> findActionsByGoalId(Long goalId);

	int saveAll(List<Action> action);

	List<Action> selectAllByAccount(@Param("accountId")Long accountId);

	Action findByGoalId(@Param("goalId") Long goalId);

	Action selectActionWithAllDetails(@Param("actionId") Long actionId);

	List<Action> findAllByAccountId(@Param("accountId") Long accountId);
}
