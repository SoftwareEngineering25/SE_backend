package com.dr.controller.user;

import com.dr.dto.user.*;
import com.dr.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

import static com.dr.service.user.RandomStringGenerator.generateRandomString;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/")
public class UserController {
    private final UserService userService;

    // dr회원가입 페이지 이동
    @GetMapping("/user/drJoin")
    public String drJoinPage() {
        return "/user/drJoin";
    }

    // 로그인 페이지 이동
    @GetMapping("/user/login")
    public String loginPage() {
        return "/user/login";
    }

    // api회원가입 페이지 이동
    @GetMapping("/user/apiJoin")
    public String apiJoinPage() {
        return "/user/apiJoin";
    }

    // 아이디 찾기 페이지 이동
    @GetMapping("/user/emailFind")
    public String emailFindPage() {
        return "/user/emailFind";
    }

    // 아이디 찾기 완료 페이지 이동
    @GetMapping("/user/emailFindFinish")
    public String emailFindFinishPage() {
        return "/user/emailFindFinish";
    }

    // 회원가입 종류 페이지 이동
    @GetMapping("/user/join")
    public String joinPage() {
        return "/user/join";
    }


    // 비밀번호 찾기 페이지 이동
    @GetMapping("/user/PwFind")
    public String PwFindPage() {
        return "/user/PwFind";
    }

    // 비밀번호 재설정 페이지 이동
    @GetMapping("/user/PwReset")
    public String PwResetPage() {
        return "/user/PwReset";
    }


    // 아이디 찾기 완료 페이지 이동
    @PostMapping("/user/emailFindOk")
    public String emailFindPage(@RequestParam("phone") String userPhone, Model model) {
        EmailFindDTO userEmail = userService.userFindEmail(userPhone);

        if (userEmail == null) {
            return "redirect:/user/emailFind"; // 전화번호가 일치하지 않으면 다시 이메일 찾기 페이지로 리다이렉트
        }

        model.addAttribute("userEmail", userEmail);
        return "/user/emailFindFinish"; // 이메일 찾기 완료 페이지로 이동
    }


    //비밀번호 찾기 페이지
    @PostMapping("/user/PwFind")
    public String PwFind(HttpSession session, Model model,
                         @RequestParam("userEmail") String userEmail,
                         @RequestParam("userPhone") String userPhone) {
        PwFindDTO pwFindDTO = userService.userPwFind(userEmail, userPhone);
        log.info("userController PwFind 메소드 - " + pwFindDTO);

        if (pwFindDTO != null && pwFindDTO.getUserPhone() != null) {
            model.addAttribute("userEmail", pwFindDTO.getUserEmail());
            session.setAttribute("userPhone", pwFindDTO.getUserPhone()); // userPhone을 세션에 저장
            return "redirect:/user/PwReset";
        } else {
            model.addAttribute("errorMessage", "이메일 또는 핸드폰 번호가 일치하지 않습니다.");
            return "/user/PwFind"; // 다시 비밀번호 찾기 페이지로 이동
        }
    }



    //비밀번호 변경 페이지
    @PostMapping("/user/PwReset")
    public String PwReset(HttpSession session, Model model,
                          @RequestParam("confirmNewPassword") String userPw) {
        String userPhone = (String) session.getAttribute("userPhone"); // 세션에서 userPhone 가져오기
        log.info("비밀번호 변경 요청 - userPhone: " + userPhone); // 전화번호 확인 로그

        if (userPhone != null && !userPhone.isEmpty()) {
            userService.updatePassword(userPw, userPhone); // 비밀번호 업데이트
        } else {
            log.error("사용자 전화번호가 비어 있습니다."); // 에러 로그
            model.addAttribute("errorMessage", "전화번호가 유효하지 않습니다.");
            return "user/PwReset"; // 다시 비밀번호 변경 페이지로 돌아감
        }

        session.removeAttribute("userPhone"); // 세션에서 userPhone 제거
        return "redirect:/user/login"; // 로그인 페이지로 리다이렉트
    }



    //drjoin 회원가입 요청 컨트롤러
    @PostMapping("/user/drJoin")
    public String join(@ModelAttribute UserDTO userDTO) {
        // 가입페이지 입력값 테이블에 저장
        userService.registerUser(userDTO);

        // 방금 가입유저 번호 저장
        userDTO.setUserNumber(userService.findNewUser(userDTO));

        // 포인트,점수,기본프사 row 생성
        userService.insertNewUserPoint(userDTO);
        userService.insertNewUserScore(userDTO);
        userService.insertNewUserPhoto(userDTO);

        return "redirect:/user/login";
    }

    //apijoin 회원가입 요청 컨트롤러
    @PostMapping("/user/apiJoinOK")
    public String kakaoJoin(@ModelAttribute KakaoUsersDTO kakaoUsersDTO,
                            HttpSession session) {
        //api회원은 비밀번호입력 안하므로 랜덤한 비밀번호생성
        kakaoUsersDTO.setUserPw(generateRandomString(10));
        userService.insertKakaoUser(kakaoUsersDTO);

        // 유저번호 가져오기 위한 로그인 쿼리 실행
        UserSessionDTO userLogin = userService.userLogin(kakaoUsersDTO.getAccountEmail(), kakaoUsersDTO.getUserPw());

        // 포인트, 점수 row 넣어주기
        UserDTO userDTO = new UserDTO();
        userDTO.setUserNumber(userLogin.getUserNumber());
        userService.insertNewUserPoint(userDTO);
        userService.insertNewUserScore(userDTO);

        if (userLogin != null) {
            // 세션에 사용자 정보를 설정
            session.setAttribute("userNumber", userLogin.getUserNumber());
            session.setAttribute("userNickName", userLogin.getUserNickName());
            session.setAttribute("providerId", kakaoUsersDTO.getProviderId());
            session.setAttribute("profilePic", kakaoUsersDTO.getProfilePic());

            return "redirect:/DRmain";

        } else {
            return "user/login";
        }
    }


    // 이메일 중복 확인 요청 처리
    @GetMapping("/api/user/checkEmail")
    @ResponseBody // 특정 메서드만 JSON 형식 응답 반환
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam("userEmail") String userEmail) {
        boolean exists = userService.isEmailExists(userEmail);
        return ResponseEntity.ok(exists);
    }

    // 핸드폰 중복 확인 요청 처리
    @PostMapping("/api/user/checkPhone")
    @ResponseBody
    public Map<String, Boolean> checkPhone(@RequestBody Map<String, String> request) {
        String userPhone = request.get("userPhone");
        boolean exists = userService.isPhoneExists(userPhone);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return response;
    }

    // 로그인 페이지
    @PostMapping("/login") // @PostMapping("/user/login") 또는 @PostMapping("/login") (RequestMapping에 따라 다름)
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDTO loginDTO, // @RequestBody와 UserDTO 사용
                                                     HttpSession session) {
        // DTO 객체에서 이메일과 비밀번호를 가져와 사용
        String userEmail = loginDTO.getUserEmail();
        String userPw = loginDTO.getUserPw();

        log.info("로그인 시도 확인: {}", userEmail); // 이제 userEmail에 값이 들어올 것입니다.

        UserSessionDTO userLogin = userService.userLogin(userEmail, userPw);
        Map<String, String> response = new HashMap<>();

        if (userLogin != null) {
            // 세션에 사용자 정보를 설정
            session.setAttribute("userNumber", userLogin.getUserNumber());
            session.setAttribute("userNickName", userLogin.getUserNickName());
            session.setAttribute("photoLocal", userLogin.getPhotoLocal());

            // 로그인 성공 시 성공 메시지와 리다이렉트 URL 반환
            response.put("message", "환영합니다.");
            response.put("redirect", "/DRmain");
            return ResponseEntity.ok(response);
        } else {
            // 로그인 실패 시 오류 메시지와 리다이렉트 URL 반환
            response.put("message", "이메일이나 비밀번호가 잘못되었습니다.");
            response.put("redirect", "/user/login"); // 로그인 페이지로 리다이렉트
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // 401 Unauthorized
        }
    }


    // 로그아웃 요청 처리
    @GetMapping("/DRlogout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/DRmain");
    }

    // 닉네임 검사
    @PostMapping("/checkNickName")
    public ResponseEntity<Integer> checkNickName(@RequestParam("userNickName") String userNickName){
        return ResponseEntity.ok(userService.checkNickName(userNickName));
    }

}


