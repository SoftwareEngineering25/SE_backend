package com.dr.controller.manager;

import com.dr.service.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // ResponseEntity 임포트
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerRestController {

    private final ManagerService managerService;


    // 1. 회원 탈퇴
    @DeleteMapping("/userOut")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> userNumbers = request.get("userNumber");
        boolean allDeleted = true;

        for (Integer userNumber : userNumbers) {
            if (!managerService.userOut(userNumber)) {
                allDeleted = false;
            }
        }

        if (allDeleted) {
            return ResponseEntity.ok("선택된 사용자가 탈퇴 처리되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일부 사용자 삭제에 실패했습니다.");
        }
    }

    // 1. 회원 정지
    @PutMapping("/userPause")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> userNumbers = request.get("userNumber");
        boolean allUpdated = true;

        for (Integer userNumber : userNumbers) {
            if (!managerService.userPause(userNumber)) {
                allUpdated = false; // 하나라도 실패하면 false로 설정
            }
        }

        if (allUpdated) {
            return ResponseEntity.ok("선택된 사용자가 정지 처리되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일부 사용자 정지에 실패했습니다.");
        }
    }


    // 2. 게시글 삭제
    @DeleteMapping("/boardDelete")
    public ResponseEntity<?> boardDelete(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> boardNumbers = request.get("boardNumber");
        boolean allDeleted = true;

        for (Integer boardNumber : boardNumbers) {
            if (!managerService.boardDelete(boardNumber)) {
                allDeleted = false; // 하나라도 실패하면 false로 설정
            }
        }

        if (allDeleted) {
            return ResponseEntity.ok("선택된 게시글이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일부 게시글 삭제에 실패했습니다.");
        }
    }

    // 3. 레시피 삭제
    @DeleteMapping("/recipeDelete")
    public ResponseEntity<?> recipeDelete(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> recipeNumbers = request.get("recipeNumber");
        boolean allDeleted = true;

        for (Integer recipeNumber : recipeNumbers) {
            if (!managerService.recipeDelete(recipeNumber)) {
                allDeleted = false; // 하나라도 실패하면 false로 설정
            }
        }

        if (allDeleted) {
            return ResponseEntity.ok("선택된 레시피가 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일부 레시피 삭제에 실패했습니다.");
        }
    }

    // 4. 댓글 삭제
    @DeleteMapping("/replyDelete")
    public ResponseEntity<?> replyDelete(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> replyNumbers = request.get("replyNumber");
        boolean allDeleted = true;

        for (Integer replyNumber : replyNumbers) {
            if (!managerService.replyDelete(replyNumber)) {
                allDeleted = false; // 하나라도 실패하면 false로 설정
            }
        }

        if (allDeleted) {
            return ResponseEntity.ok("선택된 댓글이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일부 댓글 삭제에 실패했습니다.");
        }
    }

    // 5. 포인트 삭제
    @DeleteMapping("/pointDelete")
    public ResponseEntity<?> pointDelete(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> pointNumbers = request.get("pointNumber");
        boolean allDeleted = true;

        for (Integer pointNumber : pointNumbers) {
            if (!managerService.pointDelete(pointNumber)) {
                allDeleted = false; // 하나라도 실패하면 false로 설정
            }
        }

        if (allDeleted) {
            return ResponseEntity.ok("선택된 포인트가 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일부 포인트 삭제에 실패했습니다.");
        }
    }

    // 5. 포인트 회수
    @PutMapping("/takePoint")
    public ResponseEntity<?> takePoint(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> pointNumbers = request.get("pointNumber");
        boolean allUpdated = true;

        for (Integer pointNumber : pointNumbers) {
            if (!managerService.takePoint(pointNumber)) {
                allUpdated = false; // 하나라도 실패하면 false로 설정
            }
        }

        if (allUpdated) {
            return ResponseEntity.ok("선택된 포인트가 회수되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일부 포인트 회수에 실패했습니다.");
        }
    }


    // 6. 신고 삭제
    @DeleteMapping("/deleteReport")
    public ResponseEntity<?> deleteReport(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> sirenNumbers = request.get("sirenNumber");
        boolean allDeleted = true;

        for (Integer sirenNumber : sirenNumbers) {
            if (!managerService.reportDelete(sirenNumber)) {
                allDeleted = false; // 하나라도 실패하면 false로 설정
            }
        }

        if (allDeleted) {
            return ResponseEntity.ok("선택된 신고가 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일부 신고 삭제에 실패했습니다.");
        }
    }

    // 7. 상품 삭제
    @DeleteMapping("/deleteProduct")
    public ResponseEntity<String> deleteProduct(@RequestBody Map<String, List<String>> request) {
        List<String> productNames = request.get("productName");
        boolean allDeleted = true;

        for (String productName : productNames) {
            if (!managerService.productDelete(productName)) {
                allDeleted = false; // 하나라도 실패하면 false로 설정
            }
        }

        if (allDeleted) {
            return ResponseEntity.ok("선택된 상품이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("일부 상품 삭제에 실패했습니다.");
        }
    }

















}
