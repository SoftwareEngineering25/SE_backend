package com.dr.controller.myPage;

import com.dr.service.myPage.MyPageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MyPageControllerTest {

    @InjectMocks
    private MyPageController myPageController;  // 테스트할 Controller

    @Mock
    private MyPageService myPageService;  // Service Mock 객체

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화
    }

    @Test
    void checkFullAttendance_Success() {
        // given: 사용자 번호와 출석 체크 성공 시 메시지
        Long userNumber = 1L;
        String expectedMessage = "이번 달 개근을 달성하셨습니다! 200 포인트가 적립되었습니다.";

        // when: Service 메서드 호출 결과 설정
        when(myPageService.monthFullCheck(userNumber)).thenReturn(true);  // 출석 체크 성공 시 true 반환

        // when: Controller 메서드 호출
        String result = myPageController.checkFullAttendance(userNumber);

        // then: 반환 메시지가 예상한 메시지와 일치하는지 확인
        assertEquals(expectedMessage, result);

        // 출력
        System.out.println("테스트 결과: " + result);
    }
}
