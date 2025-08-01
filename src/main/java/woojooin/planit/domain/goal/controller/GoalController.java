package woojooin.planit.domain.goal.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import woojooin.planit.global.exception.BusinessException;
import woojooin.planit.global.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import woojooin.planit.domain.goal.domain.Goal;
import woojooin.planit.domain.goal.dto.GoalRequestDto;
import woojooin.planit.domain.goal.service.GoalSettingService;
import woojooin.planit.global.response.ResponseCode;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth/api/goals")
@RequiredArgsConstructor
@Api(value = "목표 API" ,description = "목표 설정 및 조회 관련 API ")
public class GoalController {
    private final GoalSettingService goalService;

    private Long getCurrentAuthenticatedUserId() {
        // todo 실제 인증 처리 필요
        return 1L; // 임시 값 사용자 ID
    }

    @PostMapping
    @ApiOperation(value = "목표 생성" , notes = "사용자의 새로운 목표를 생성합니다." )
    public ResponseEntity<Response<Goal>> createGoal(@Valid @RequestBody GoalRequestDto dto) {
        Long memberID = getCurrentAuthenticatedUserId();
        Goal goal =dto.toEntity();
        goal.setMemberId(memberID);
        goalService.createGoal(memberID,goal);
        return ResponseEntity.ok(Response.ok(goal));
    }

    @GetMapping
    @ApiOperation(value = "목표리스트 조회" , notes = "사용자의 모든 목표를 조회합니다")
    public ResponseEntity<Response<List<Goal>>> getAllGoals() {
        Long userID = getCurrentAuthenticatedUserId();
        List<Goal> goals = goalService.getGoals(userID);
        return ResponseEntity.ok(Response.ok(goals));
    }

    @GetMapping("/{goalsId}")
    @ApiOperation(value = "목표 조회 ", notes = "사용자의 특정 목표를 조회합니다")
    public ResponseEntity<Response<Goal>> getGoal(@PathVariable Long goalsId) {
        Long userID = getCurrentAuthenticatedUserId();
        Optional<Goal> goal = goalService.getGoal(userID, goalsId);
        Goal foundGoal = goal.orElseThrow(() -> new BusinessException(ResponseCode.NOT_FOUND));
        return ResponseEntity.ok(Response.ok(foundGoal));

    }

    @PutMapping("/{goalId}")
    @ApiOperation(value = "목표 수정",notes = "사용자의 목표를 수정합니다")
    public ResponseEntity<Response<Void>> updateGoal(@PathVariable Long goalId, @RequestBody GoalRequestDto dto) {
        Long userId = getCurrentAuthenticatedUserId();
        Goal goal = dto.toEntity();
        goal.setObjectId(goalId);
        goalService.updateGoal(goalId, userId, goal);
        return ResponseEntity.ok(Response.ok());
    }

    @DeleteMapping("/{goalId}")
    @ApiOperation(value = "목표 삭제", notes = "사용자의 목표를 삭제합니다")
    public ResponseEntity<Response<Void>> deleteGoal(@PathVariable Long goalId) {
        Long currentUserId = getCurrentAuthenticatedUserId();
        int rowsAffected = goalService.deleteGoal(goalId, currentUserId);
        if (rowsAffected == 0) {
            throw new BusinessException(ResponseCode.NOT_FOUND);
        }
        return ResponseEntity.ok(Response.ok());
    }
}
