package woojooin.planit.domain.goal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import woojooin.planit.domain.goal.domain.Goal;
import woojooin.planit.domain.goal.service.GoalSettingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {
    private final GoalSettingService goalService;

    private Long getCurrentAuthenticatedUserId() {
        // todo 실제 인증 처리 필요
        return 1L; // **임시 값 사용자 ID**
    }


    /*목표 생성  ( /api/goals )  */
    @PostMapping
    public ResponseEntity<Goal> createGoal(@RequestBody Goal goal) {
        Long memberID = getCurrentAuthenticatedUserId();
        goal.setMemberId(memberID);
        try{
            int result = goalService.createGoal(memberID,goal);
            if(result>0){
                return new ResponseEntity<>(goal,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   /* 모든 목표 조회 (/api/goals)*/
    @GetMapping
    public ResponseEntity<List<Goal>> getAllGoals() {
        Long userID = getCurrentAuthenticatedUserId();
        List<Goal> goals = goalService.getGoals(userID);
        return new ResponseEntity<>(goals,HttpStatus.OK);
    }
   /*특정 목표 1개 조회하기 (/api/goals/{goalId}*/
    @GetMapping("/{goalsId}")
    public ResponseEntity<Goal> getGoal(@PathVariable Long goalsId) {
        Long userID = getCurrentAuthenticatedUserId();
        Optional<Goal> goal = goalService.getGoal(userID, goalsId);
        return goal.map(goal1 -> new ResponseEntity<>(goal1,HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    //목표 수정 (/api/goals/{id})
    @PutMapping("/{goalId}")
    public ResponseEntity<Void> updateGoal(@PathVariable Long goalId, @RequestBody Goal goal) {
        Long userId = getCurrentAuthenticatedUserId();
        try {
            goal.setObjectId(goalId);
            goal.setMemberId(userId);
            int result = goalService.updateGoal(goalId,userId,goal);
            return new ResponseEntity<>(HttpStatus.OK); // 200 OK
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long goalId) {
        Long currentUserId = getCurrentAuthenticatedUserId();
        try {
            // 서비스 계층에서 목표 삭제 (goalId와 currentUserId를 기준으로 검증)
            int rowsAffected = goalService.deleteGoal(goalId, currentUserId);
            if (rowsAffected > 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content (성공적으로 처리되었으나 반환할 내용 없음)
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found (대상 목표 없거나 권한 없음)
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error deleting goal: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.err.println("Unexpected error deleting goal: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }
}
