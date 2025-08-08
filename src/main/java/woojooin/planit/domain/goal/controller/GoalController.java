package woojooin.planit.domain.goal.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import woojooin.planit.domain.goal.dto.GoalDetailResponseDto;
import woojooin.planit.domain.goal.dto.GoalProgressGraphDTO;
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
        goalService.createGoal(memberID,goal);
        return ResponseEntity.ok(Response.ok(goal));
    }

    @GetMapping
    @ApiOperation(value = "목표리스트 조회", notes = "사용자의 모든 목표(ISA 포함)를 상세 조회합니다")
    public ResponseEntity<Response<List<GoalDetailResponseDto>>> getAllGoals() {
        Long userID = getCurrentAuthenticatedUserId();
        List<GoalDetailResponseDto> goals = goalService.getAllGoals(userID);
        return ResponseEntity.ok(Response.ok(goals));
    }

    @GetMapping("/{goalId}")
    @ApiOperation(value = "목표 조회 ", notes = "사용자의 특정 목표를 조회합니다")
    public ResponseEntity<Response<GoalDetailResponseDto>> getGoalDetail(@PathVariable Long goalId) {
        Long userId = getCurrentAuthenticatedUserId();
        GoalDetailResponseDto response = goalService.getGoalDetail(userId, goalId);
        return ResponseEntity.ok(Response.ok(response));

    }

    @PutMapping("/{goalId}")
    @ApiOperation(value = "목표 수정",notes = "사용자의 목표를 수정합니다")
    public ResponseEntity<Response<GoalDetailResponseDto>> updateGoal(@PathVariable Long goalId, @RequestBody GoalRequestDto dto) {
        Long userId = getCurrentAuthenticatedUserId();
        Goal goal = dto.toEntity();
        GoalDetailResponseDto response = goalService.updateGoal(goalId, userId, goal);
        return ResponseEntity.ok(Response.ok(response));
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

    @GetMapping("/progress")
    @ApiOperation(value = "목표 진행 추이", notes = "사용자 목표 진행 추이를 그래프로 나타냄니다.")
    public Response<List<GoalProgressGraphDTO>> getGoalProgressList(int goalId) {
        List<GoalProgressGraphDTO> list = goalService.getGoalProgressByGoalId(goalId)
        return Response.ok(list);
    }
}
