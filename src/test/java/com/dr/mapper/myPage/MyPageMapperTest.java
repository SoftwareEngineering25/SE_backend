package com.dr.mapper.myPage;

import com.dr.dto.myPage.PointDetailDTO;
import com.dr.dto.myPage.UserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 모든 빈을 로드하여 통합 테스트 환경을 구성
@Transactional // 각 테스트 종료 시 데이터베이스 상태를 원래대로 롤백
@Slf4j // 로깅 기능을 제공하는 Lombok 애노테이션
class MyPageMapperTest {

    @Autowired
    MyPageMapper myPageMapper; // MyPageMapper를 자동 주입

    @BeforeEach
    void setUp() {
        // 테스트에 사용할 UserInfoDTO 객체를 설정합니다.
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserNumber(1L); // 유저 번호 설정
        userInfoDTO.setUserNickName("아성이에요"); // 유저 닉네임 설정
        userInfoDTO.setUserEmail("user1@dr.com"); // 유저 이메일 설정
        userInfoDTO.setUserPhone("01012341234"); // 유저 전화번호 설정
        userInfoDTO.setEnvironmentScore(615); // 환경 점수 설정
        userInfoDTO.setEnvironmentRank(1); // 환경 순위 설정
        userInfoDTO.setTotalPoints(40); // 총 포인트 설정
        userInfoDTO.setPhoto("뭐먹지.png"); // 사진 경로 설정
    }

    @Test
    //---- 내 정보 확인 테스트 ---- //
    void getUserInfo() {
        // given
        Long testUserNumber = 1L; // 테스트할 유저 번호

        // when
        UserInfoDTO userInfoDTO = myPageMapper.getUserInfo(testUserNumber); // 유저 정보를 가져옴

        // then
        // 유저 정보가 예상한 값과 일치하는지 검증
        assertEquals("아성이에요", userInfoDTO.getUserNickName(), "닉네임 검증");
        assertEquals("user1@dr.com", userInfoDTO.getUserEmail(), "이메일 검증");
        assertEquals("01012341234", userInfoDTO.getUserPhone(), "전화번호 검증");
        assertEquals(615, userInfoDTO.getEnvironmentScore(), "환경 점수 검증");
        assertEquals(1, userInfoDTO.getEnvironmentRank(), "환경 순위 검증");
        assertEquals(40, userInfoDTO.getTotalPoints(), "총 포인트 검증");
        assertEquals("뭐먹지.png", userInfoDTO.getPhoto(), "사진 경로 검증");

        // 로그로 출력하여 확인
        log.info("로그 출력해보기 : {}", userInfoDTO);
    }

    //---- 닉네임 중복 확인 테스트 ---- //
    @Test
    void checkNickname() {
        // given
        String existingNickname = "테스트"; // 이미 존재하는 닉네임

        // when
        int existingNicknameCount = myPageMapper.checkNickname(existingNickname); // 기존 닉네임 중복 체크

        // then
        // 기존 닉네임과 중복 시 1로 나오고, 기존 닉네임과 중복되지 않을 시 0으로 나옴
        assertEquals(1, existingNicknameCount, "기존 닉네임과 중복");

        // 로그로 출력하여 확인
        log.info("기존 닉네임 중복 여부: {}", existingNicknameCount);
    }


    //---- 회원 탈퇴 테스트 ---- //
    @Test
    void updateNickname() {
        // given
        Long userNumber = 1L; // 업데이트할 유저 번호
        String existingNickname = "아성이에요"; // 기존 닉네임
        String newNickname = "어이쿠야"; // 업데이트할 새로운 닉네임

        // 기존 닉네임 확인
        UserInfoDTO userInfoDTOBefore = myPageMapper.getUserInfo(userNumber); // 유저 정보 가져오기
        assertEquals(existingNickname, userInfoDTOBefore.getUserNickName(), "기존 닉네임이 맞지 않음");

        // when
        int existingNicknameCount = myPageMapper.checkNickname(newNickname); // 닉네임 중복 체크
        if (existingNicknameCount == 0) { // 중복되지 않으면 업데이트
            myPageMapper.updateNickname(userNumber, newNickname); // 닉네임 업데이트
        } else {
            log.error("새로운 닉네임이 이미 존재합니다. 중복된 닉네임을 사용할 수 없습니다.");
        }

        // then
        UserInfoDTO userInfoDTOAfter = myPageMapper.getUserInfo(userNumber); // 업데이트 후 유저 정보 확인
        assertEquals(newNickname, userInfoDTOAfter.getUserNickName(), "닉네임 업데이트가 적용되지 않음");

        // 로그로 출력하여 확인
        log.info("업데이트 전 닉네임: {}", userInfoDTOBefore.getUserNickName());
        log.info("업데이트 후 닉네임: {}", userInfoDTOAfter.getUserNickName());
    }


    @Test
    void deleteUser() {

        // given
        Long userNumber = 1L; // 삭제할 유저 번호

        // 유저 존재 여부 확인 (삭제 전)
        UserInfoDTO userInfoDTOBefore = myPageMapper.getUserInfo(userNumber); // 유저 정보 가져오기
        assertNotNull(userInfoDTOBefore, "삭제 전 유저 정보가 확인");

        // when
        myPageMapper.deleteUser(userNumber); // 회원 탈퇴 처리

        // then
        // 삭제 후 유저 정보가 존재하지 않는지 확인
        UserInfoDTO userInfoDTOAfter = myPageMapper.getUserInfo(userNumber); // 삭제 후 유저 정보 확인
        assertNull(userInfoDTOAfter, "유저가 삭제되어야 합니다."); // 유저 정보가 null이어야 함

        // 로그로 출력하여 확인
        log.info("회원 탈퇴 처리 후, 유저 정보: {}", userInfoDTOAfter);




    }

    @Test
    void pointHistory() {
        // given
        Long userNumber = 1L; // 테스트할 유저 번호

        // 예상 포인트 내역 설정 (PointDetailDTO 객체로 생성)
        List<PointDetailDTO> expectedPointHistory = List.of(
                new PointDetailDTO() {{
                    setPointNumber(1);
                    setPointNote("출석 포인트");
                    setPointGet(200);
                    setPointDate(LocalDateTime.of(2024, 1, 1, 0, 0));
                    setTotalPoints(200);
                }},
                new PointDetailDTO() {{
                    setPointNumber(2);
                    setPointNote("리뷰 작성 포인트");
                    setPointGet(150);
                    setPointDate(LocalDateTime.of(2024, 1, 2, 0, 0));
                    setTotalPoints(350);
                }}
        );

        // when
        // 실제 포인트 내역 조회
        List<PointDetailDTO> actualPointHistory = myPageMapper.pointHistory(userNumber);

        // then
        // 예상 결과와 실제 결과의 크기 비교
        assertNotNull(actualPointHistory, "포인트 내역이 null이 아님을 확인해야 합니다.");
        assertEquals(expectedPointHistory.size(), actualPointHistory.size(), "포인트 내역의 항목 수가 일치해야 합니다.");

        // 각 포인트 항목의 상세 내용 비교
        for (int i = 0; i < expectedPointHistory.size(); i++) {
            PointDetailDTO expected = expectedPointHistory.get(i);
            PointDetailDTO actual = actualPointHistory.get(i);

            assertEquals(expected.getPointNumber(), actual.getPointNumber(), "포인트 번호가 일치해야 합니다.");
            assertEquals(expected.getPointNote(), actual.getPointNote(), "포인트 내용이 일치해야 합니다.");
            assertEquals(expected.getPointGet(), actual.getPointGet(), "포인트 획득 값이 일치해야 합니다.");
            assertEquals(expected.getFormattedPointDate(), actual.getFormattedPointDate(), "포인트 날짜가 포맷된 형식으로 일치해야 합니다.");
            assertEquals(expected.getTotalPoints(), actual.getTotalPoints(), "총 포인트가 일치해야 합니다.");
        }

        // 로그로 결과 출력
        log.info("포인트 내역 테스트 결과: {}", actualPointHistory);
    }

}
