        package woojooin.planit.domain.goal.controller;
        import io.swagger.annotations.Api;
        import io.swagger.annotations.ApiOperation;
        import org.springframework.security.core.annotation.AuthenticationPrincipal;
        import woojooin.planit.domain.goal.dto.GoalDetailResponseDto;
        import woojooin.planit.domain.goal.service.GoalSettingService;
        import woojooin.planit.global.exception.BusinessException;
        import woojooin.planit.global.response.Response;
        import lombok.RequiredArgsConstructor;
        import woojooin.planit.domain.goal.dto.GoalAccountRateResponse;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;
        import woojooin.planit.domain.goal.domain.Goal;
        import woojooin.planit.domain.goal.dto.GoalRequestDto;
        import woojooin.planit.global.response.ResponseCode;
        import woojooin.planit.global.security.CustomUserDetails;

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
                return 1L;
            }

            @PostMapping
            @ApiOperation(value = "목표 생성" , notes = "사용자의 새로운 목표를 생성합니다." )
            public ResponseEntity<Response<Goal>> createGoal(
                    @Valid @RequestBody GoalRequestDto dto){
                    //@AuthenticationPrincipal CustomUserDetails userDetails) {
                    //userDetails.getId()
                Long memberId = getCurrentAuthenticatedUserId();
                Goal createdGoal = goalService.createGoal(memberId, dto);
                return ResponseEntity.ok(Response.ok(createdGoal));
            }

            @GetMapping
            @ApiOperation(value = "목표리스트 조회", notes = "사용자의 모든 목표(ISA 포함)를 상세 조회합니다")
            public ResponseEntity<Response<List<GoalDetailResponseDto>>> getAllGoals(
                @AuthenticationPrincipal CustomUserDetails customUserDetails
                ) {
                Long userID = customUserDetails.getId();
                List<GoalDetailResponseDto> goals = goalService.getAllGoals(userID);
                return ResponseEntity.ok(Response.ok(goals));
            }

            @GetMapping("/{goalId}")
            @ApiOperation(value = "목표 조회 ", notes = "사용자의 특정 목표를 조회합니다")
            public ResponseEntity<Response<GoalDetailResponseDto>> getGoalDetail(
                    @PathVariable Long goalId){
                    //@AuthenticationPrincipal CustomUserDetails userDetails)
                Long memberId = getCurrentAuthenticatedUserId();
                GoalDetailResponseDto response = goalService.getGoalDetail(memberId, goalId);
                return ResponseEntity.ok(Response.ok(response));

            }

            @PutMapping("/{goalId}")
            @ApiOperation(value = "목표 수정",notes = "사용자의 목표를 수정합니다")
            public ResponseEntity<Response<Goal>> updateGoal(
                    @PathVariable Long goalId,
                    @RequestBody GoalRequestDto dto){
        //       @AuthenticationPrincipal CustomUserDetails userDetails) {
                Long memberId = getCurrentAuthenticatedUserId();
                Goal updatedGoal = goalService.updateGoal(memberId, goalId, dto);
                return ResponseEntity.ok(Response.ok(updatedGoal));
            }

            @DeleteMapping("/{goalId}")
            @ApiOperation(value = "목표 삭제", notes = "사용자의 목표를 삭제합니다")
            public ResponseEntity<Response<Void>> deleteGoal(
                    @PathVariable Long goalId){
        //            @AuthenticationPrincipal CustomUserDetails userDetails) {
                Long memberId = getCurrentAuthenticatedUserId();
                goalService.deleteGoal(goalId,  memberId);

                return ResponseEntity.ok(Response.ok());
            }

            @GetMapping("/{goalId}/rate")
            @ApiOperation(value = "목표 대비 계좌별 진행률", notes = "계좌 잔액과 할당 비율 기준 목표 대비 진행률(%) 반환")
            public Response<List<GoalAccountRateResponse>> getGoalAccountRates(@PathVariable("goalId") Long goalId) {
                List<GoalAccountRateResponse> list = goalService.getGoalAccountRates(goalId);
                return Response.ok(list);
            }

        }