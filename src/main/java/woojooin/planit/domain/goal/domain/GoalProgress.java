package woojooin.planit.domain.goal.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GoalProgress {
	private int dailyGoalProgressId;  // daily_goal_progress_id
	private int goalId;                // goal_id
	private double isaProgress;        // isa_progress
	private double depositProgress;    // deposit_progress
	private LocalDateTime progressDate; // progress_date
	private LocalDateTime createdAt;    // created_at
	private LocalDateTime updatedAt;    // updated_at
}
