package woojooin.planit.domain.goal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import woojooin.planit.domain.goal.domain.vo.Action;

@Mapper
public interface ActionMapper {
	List<Action> findActionsByGoalId(Long goalId);

	int saveAll(List<Action> action);
}
