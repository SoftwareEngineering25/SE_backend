package com.dr.controller.myPage;

import com.dr.dto.myPage.*;
import com.dr.service.myPage.MyPageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor // 생성자를 자동 생성
@RequestMapping("/myPage") // 기본 url 설정
@Slf4j //로그 출력하려고...
public class MyPageController {

    private final MyPageService myPageService;

    // -- 내 정보 확인하기 --
    @GetMapping("/myPageInformation")
    public String getUserInfo(@SessionAttribute(value = "userNumber", required = false) Long userNumber, Model model) {
        // value = userNumber는 세션에 저장된 값
        // userNumber가 없을 경우 null이 나오고, required = true로 설정하면 "userNumber" 세션이 없을 때 예외 발생
        // 세션에 userNumber가 없는 경우 로그인 페이지로 리다이렉트

        UserInfoDTO userInfo = myPageService.getUserInfo(userNumber);
        model.addAttribute("userInfo", userInfo);
        //html에서 타임리프로 사용하기위해 userInfo 객체를 model에 추가

        return "myPage/myPageInformation";


    }

    //닉네임 중복 확인
    @PostMapping("/checkNickname")
    public ResponseEntity<Boolean> checkNickname(@RequestBody Map<String, String> requestBody) {
        String nickname = requestBody.get("nickname");
        String currentNickname = requestBody.get("currentNickname"); // 현재 사용자의 닉네임

        // 닉네임 중복 확인
        boolean isAvailable = myPageService.checkNickname(nickname, currentNickname);

        if (isAvailable) {
            return ResponseEntity.ok(false); //중복된 닉네임
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(true);
            // HTTP 응답 상태를 409(CONFLICT)로 설정하고 true를 반환합// 사용 가능
        }
    }

    // -- 이미지 변경 -- //
    @PostMapping("/updateProfile")
    public ResponseEntity<Map<String, String>> updateProfile(
            @SessionAttribute("userNumber") Long userNumber,
            @RequestParam("nickname") String nickname,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage, Model model) throws IOException {

        Map<String, String> response = new HashMap<>();

        // 사용자 정보를 가져오기
        UserInfoDTO userInfo = myPageService.getUserInfo(userNumber);
        // 업데이트된 userInfo를 Model에 추가
        model.addAttribute("userInfo", userInfo); // HTML에서 userInfo로 접근 가능


        // 닉네임이 입력되면 닉네임을 업데이트
        if (nickname != null && !nickname.isEmpty()) {
            myPageService.updateNickname(userNumber, nickname);
            response.put("nickname", nickname);
        }

        // 이미지 파일이 있으면 저장하기
        if (profileImage != null && !profileImage.isEmpty()) {
            String photoPath = myPageService.updateProfileImage(userNumber, profileImage);
            // 파일 경로 URL 인코딩
            String encodedPhotoPath = URLEncoder.encode(photoPath, "UTF-8");
            response.put("photoPath", encodedPhotoPath);
        }

        // JSON 형태로 응답 반환
        return ResponseEntity.ok().body(response);

    }


    // -- 회원탈퇴 주의사항 -- //
    @GetMapping("/myPageCaution")
    public String showCautionPage() {
        return "myPage/myPageCaution";
    }

    // -- 회원탈퇴 페이지 -- //
    @PostMapping("/myPageUserDeleted")
    public String deleteUser(@SessionAttribute(value = "userNumber", required = false) Long userNumber,
                             HttpSession session) {

        myPageService.deleteUser(userNumber);
        session.invalidate();
        return "redirect:/myPage/myPageDeleted";
    }

    @GetMapping("/myPageDeleted")
    public String showDeletedPage() {
        return "myPage/myPageDeleted";
    }

    // -- 내정보 포인트 내역 확인 -- //
    @GetMapping("/myPageMyPoint")
    public String getPointHistory(@SessionAttribute(value = "userNumber", required = false) Long userNumber, HttpSession session, Model model) {

        List<PointDetailDTO> pointHistory = myPageService.pointHistory(userNumber);
        model.addAttribute("pointHistory", pointHistory);

        return "myPage/myPageMyPoint";
    }

    // -- 내정보 내가 쓴 레시피 확인 -- //
    @GetMapping("/myPageMyRecipe")
    public String getUserRecipes(@SessionAttribute(value = "userNumber", required = false) Long userNumber, Model model) {

        // 사용자가 쓴 레시피 목록 가져오기
        List<UserRecipeDTO> userRecipes = myPageService.getUserRecipe(userNumber);
        model.addAttribute("userRecipes", userRecipes);

        return "myPage/myPageMyRecipe";
    }

    // -- 내정보 내가 쓴 게시글 목록 확인 -- //
    @GetMapping("/myPageMyPost")
    public String getUserPosts(@SessionAttribute(value = "userNumber", required = false) Long userNumber, Model model) {

        // 사용자가 쓴 게시글 목록 가져오기
        List<UserPostDTO> userPosts = myPageService.getUserPost(userNumber);
        model.addAttribute("userPosts", userPosts);

        return "myPage/myPageMyPost";
    }

    // -- 내정보 찜 목록 확인 --
    @GetMapping("/myPageSteamedList")
    public String getUserSteam(@SessionAttribute(value = "userNumber", required = false) Long userNumber, Model model) {

        List<UserSteamDTO> userSteamList = myPageService.getUserSteam(userNumber);
        System.out.println("찜 번호 : " + userSteamList);
        model.addAttribute("userSteamList", userSteamList);

        return "myPage/myPageSteamedList";
    }

//    // -- 찜삭제 DeleteMapping -- //
//    @DeleteMapping("/steamedDelete")
//    public ResponseEntity<Void> deleteSteam(
//            @SessionAttribute(value = "userNumber", required = false) Long userNumber,
//            @RequestBody UserSteamDTO userSteamDTO) {
//
//        userSteamDTO.setUserNumber(userNumber);
//        myPageService.deleteUserSteam(userSteamDTO);
//
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 성공적으로 삭제한 경우
//    }

    // -- 신고 내역 목록 -- //
    @GetMapping("/myPageMyComplaint")
    public String getSirenList(@SessionAttribute(value = "userNumber", required = false) Long userNumber, Model model) {
        // 세션에 userNumber가 없는 경우 로그인 페이지로 리다이렉트
        if (userNumber == null) {
            return "redirect:/user/login";
        }

        // 사용자의 신고 내역 목록 가져오기
        List<SirenListDTO> sirenList = myPageService.getSirenList(userNumber);
        model.addAttribute("sirenList", sirenList);

        return "myPage/myPageMyComplaint";
    }

    // 출석 체크 페이지로 이동
    @GetMapping("/myPageChecked")
    public String myPageChecked(Model model) {

        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("currentDate", currentDate);

        return "myPage/myPageChecked";
    }

    // 출석 체크 날짜 불러오기
    @GetMapping("/myPageAttendanceDates")
    @ResponseBody
    public List<String> getAttendanceDates(
            @SessionAttribute(value = "userNumber", required = false) Long userNumber) {

        if (userNumber != null) {
            return myPageService.getAttendanceDates(userNumber)
                    .stream()
                    .map(CheckDTO::getDailyDate) // 날짜만 추출
                    .collect(Collectors.toList());
        }
        return Collections.emptyList(); // 로그인되지 않은 경우 빈 리스트 반환
    }

    // 출석체크 포인트 적립
    @PostMapping("/myPageCheckAttendance")
    @ResponseBody
    public String checkAttendance(
            @SessionAttribute(value = "userNumber", required = false) Long userNumber,
            @RequestParam(value = "date", required = false) String date) {

        try {
            boolean isCheckedIn = myPageService.todayCheck(userNumber);

            if (!isCheckedIn) {
                myPageService.insertCheck(userNumber, date);
                PointCheckDTO pointCheckDTO = new PointCheckDTO();
                pointCheckDTO.setUserNumber(userNumber);
                pointCheckDTO.setPointGet(10);
                pointCheckDTO.setPointNote("출석체크");
                pointCheckDTO.setPointDate(date);

                myPageService.insertPointRecord(pointCheckDTO);
                return "출석 체크가 완료되었습니다. 10 포인트가 적립되었습니다.";
            } else {
                return "이미 출석 체크가 완료되었습니다.";
            }
        } catch (Exception e) {
            return "출석 체크 중 오류가 발생했습니다. 다시 시도해 주세요.";
        }
    }

    // 개근 여부 확인 및 포인트 적립
    @GetMapping("/myPageFullAttendance")
    @ResponseBody
    public String checkFullAttendance(
            @SessionAttribute(value = "userNumber", required = false) Long userNumber) {

        boolean isFullAttendance = myPageService.monthFullCheck(userNumber);

        if (isFullAttendance) {
            // 포인트 적립 로직 추가
            PointCheckDTO pointCheckDTO = new PointCheckDTO();
            pointCheckDTO.setUserNumber(userNumber);
            pointCheckDTO.setPointGet(200);
            pointCheckDTO.setPointNote("개근");
            pointCheckDTO.setPointDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            myPageService.insertPointRecord(pointCheckDTO); // 포인트 기록 삽입

            return "이번 달 개근을 달성하셨습니다! 200 포인트가 적립되었습니다.";
        } else {
            return "한 달 동안 매일 출석 시, 개근을 달성할 수 있습니다.\n"
                    + "성실한 출석으로 보상을 받아보세요!";
        }
    }
}

