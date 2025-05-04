package com.dr.api;

import com.dr.dto.myPage.UserSteamDTO;
import com.dr.service.myPage.MyPageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/myPage")  // 기본 URL을 지정해 사용자가 접근할 수 있도록 함
public class MyPageApi {
    private final MyPageService myPageService;

    public MyPageApi(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    // -- 찜 삭제 -- //
    @DeleteMapping("/steamedDelete")
    public ResponseEntity<Void> deleteSteam(
            @SessionAttribute(value = "userNumber", required = false) Long userNumber,
            @RequestBody UserSteamDTO userSteamDTO) {

        // userNumber는 세션에서 가져오고, userSteamDTO는 요청 본문에서 받음
        userSteamDTO.setUserNumber(userNumber);  // 세션에서 가져온 userNumber를 DTO에 설정

        // 찜 삭제 처리
        myPageService.deleteUserSteam(userSteamDTO);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 성공적으로 삭제한 경우
    }


}

