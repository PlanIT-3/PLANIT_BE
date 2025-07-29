package woojooin.planit.domain.goal.mapper;

import org.apache.ibatis.annotations.Mapper;
import woojooin.planit.domain.goal.domain.Goal; // 변경된 Goal VO 임포트

import java.util.List;

@Mapper
public interface GoalMapper {
    //1 . create
    int insert(Goal goal);

    //2 . read ( 단일 목표 id로 조회 / 모든 목표 조회)
    Goal selectGoalById(Long goalId);
    List<Goal> selectAllGoals(Long userId);

    //3 수정
    int updateGoal(Goal goal);

    //4 삭제
    int deleteGoal(Long goalId);
}
