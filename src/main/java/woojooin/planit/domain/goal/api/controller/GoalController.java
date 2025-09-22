        package woojooin.planit.domain.goal.api.controller;
        import io.swagger.annotations.Api;
        import io.swagger.annotations.ApiOperation;
        import org.springframework.security.core.annotation.AuthenticationPrincipal;
        import woojooin.planit.domain.goal.api.dto.res.GoalDetailRes;
        import woojooin.planit.domain.goal.service.GoalSettingService;
        import woojooin.planit.global.response.Response;
        import lombok.RequiredArgsConstructor;
        import woojooin.planit.domain.goal.api.dto.res.GoalAccountRateRes;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;
        import woojooin.planit.domain.goal.domain.vo.Goal;
        import woojooin.planit.domain.goal.api.dto.req.GoalReq;
        import woojooin.planit.global.security.CustomUserDetails;
        import woojooin.planit.domain.goal.api.dto.res.DailyGoalProgressRes;
        import javax.validation.Valid;
        import java.util.List;

        @RestController
        @RequestMapping("/auth/api/goals")
        @RequiredArgsConstructor
        @Api(value = "목표 API" ,description = "목표 설정 및 조회 관련 API ")
        public class GoalController {

            private final GoalSettingService goalService;

            @PostMapping
            @ApiOperation(value = "목표 생성" , notes = "사용자의 새로운 목표를 생성합니다." )
            public ResponseEntity<Response<Goal>> createGoal(
                    @Valid @RequestBody GoalReq dto,
                    @AuthenticationPrincipal CustomUserDetails userDetails) {
                Long memberId = userDetails.getId();
                Goal createdGoal = goalService.createGoal(memberId, dto);
                return ResponseEntity.ok(Response.ok(createdGoal));
            }

            @GetMapping
            @ApiOperation(value = "목표리스트 조회", notes = "사용자의 모든 목표(ISA 포함)를 상세 조회합니다")
            public ResponseEntity<Response<List<GoalDetailRes>>> getAllGoals(
                    @AuthenticationPrincipal CustomUserDetails customUserDetails
            ) {
                Long userID = customUserDetails.getId();
                List<GoalDetailRes> goals = goalService.getAllGoals(userID);
                return ResponseEntity.ok(Response.ok(goals));
            }

            @GetMapping("/{goalId}")
            @ApiOperation(value = "목표 조회 ", notes = "사용자의 특정 목표를 조회합니다")
            public ResponseEntity<Response<GoalDetailRes>> getGoalDetail(
                    @PathVariable Long goalId,
                    @AuthenticationPrincipal CustomUserDetails userDetails){
                Long memberId = userDetails.getId();
                GoalDetailRes response = goalService.getGoalDetail(memberId, goalId);
                return ResponseEntity.ok(Response.ok(response));

            }

            @PutMapping("/{goalId}")
            @ApiOperation(value = "목표 ",notes = "사용자의 목표를 수정합니다")
            public ResponseEntity<Response<Goal>> updateGoal(
                    @PathVariable Long goalId,
                    @RequestBody GoalReq dto,
                    @AuthenticationPrincipal CustomUserDetails userDetails) {
                Long memberId = userDetails.getId();
                Goal updatedGoal = goalService.updateGoal(memberId, goalId, dto);
                return ResponseEntity.ok(Response.ok(updatedGoal));
            }

            @DeleteMapping("/{goalId}")
            @ApiOperation(value = "목표 삭제", notes = "사용자의 목표를 삭제합니다")
            public ResponseEntity<Response<Void>> deleteGoal(
                    @PathVariable Long goalId,
                    @AuthenticationPrincipal CustomUserDetails userDetails){
                Long memberId = userDetails.getId();
                goalService.deleteGoal(goalId,  memberId);

                return ResponseEntity.ok(Response.ok());
            }


	@GetMapping("/{goalId}/rate")
	@ApiOperation(value = "목표 대비 계좌별 진행률", notes = "계좌 잔액과 할당 비율 기준 목표 대비 진행률(%) 반환")
	public Response<List<GoalAccountRateRes>> getGoalAccountRates(
        @PathVariable("goalId") Long goalId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
		List<GoalAccountRateRes> list = goalService.getGoalAccountRates(goalId);
		return Response.ok(list);
	}

    @GetMapping("/{goalId}/progress")
    @ApiOperation(value = "목표 일별 진행률 조회", notes = "특정 목표의 최근 6개월 일별 ISA/예적금 진행률 조회 (goal_id, isa_progress, deposit_progress, created_at)")
    public Response<List<DailyGoalProgressRes>> getGoalProgress(
        @PathVariable("goalId") Long goalId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<DailyGoalProgressRes> progressList = goalService.getGoalProgress(goalId);
        return Response.ok(progressList);
    }
}