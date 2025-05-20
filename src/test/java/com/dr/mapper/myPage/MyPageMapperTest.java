package com.dr.mapper.myPage;

import com.dr.dto.myPage.PointDetailDTO;
import com.dr.dto.myPage.UserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class MyPageMapperTest {

    @Autowired
    MyPageMapper myPageMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    // 테스트에 사용할 공통 사용자 정보
    private static final Long TEST_USER_NUMBER = 1L;
    private static final String TEST_USER_EMAIL = "testuser@example.com";
    private static final String TEST_USER_PW = "password123"; // 실제 PW 암호화 방식에 따라 다를 수 있음
    private static final String TEST_USER_NICKNAME = "테스트닉네임";
    private static final String TEST_USER_PHONE = "01000000000";
    private static final String TEST_USER_STATUS = "일반회원";
    private static final String TEST_PROFILE_PIC = "basicProfile.png";


    @BeforeEach
    void setUp() {
        // 기존 테스트 데이터 정리 (외래 키 제약조건 고려하여 삭제 순서 중요할 수 있음)
        // 예: 포인트, 댓글 등 USER_NUMBER를 참조하는 테이블부터 삭제
        jdbcTemplate.update("DELETE FROM DR_POINT WHERE USER_NUMBER = ?", TEST_USER_NUMBER);
        // ... 다른 자식 테이블 데이터 삭제 ...
        jdbcTemplate.update("DELETE FROM DR_USER WHERE USER_NUMBER = ?", TEST_USER_NUMBER);
        jdbcTemplate.update("DELETE FROM DR_USER WHERE USER_NICKNAME = ?", TEST_USER_NICKNAME); // 닉네임 중복 방지

        // 테스트 사용자 정보 삽입
        jdbcTemplate.update(
                "INSERT INTO DR_USER (USER_NUMBER, USER_EMAIL, USER_PW, USER_NICKNAME, USER_PHONE, USER_STATUS, PROFILE_PIC) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                TEST_USER_NUMBER, TEST_USER_EMAIL, TEST_USER_PW, TEST_USER_NICKNAME, TEST_USER_PHONE, TEST_USER_STATUS, TEST_PROFILE_PIC
        );

        // pointHistory 테스트를 위한 포인트 내역 2건 삽입
        // POINT_NUMBER는 AUTO_INCREMENT이므로 명시적으로 넣지 않음
        jdbcTemplate.update(
                "INSERT INTO DR_POINT (USER_NUMBER, POINT_NOTE, POINT_CHANGE, POINT_DATE) VALUES (?, ?, ?, ?)",
                TEST_USER_NUMBER, "출석 포인트", 200, LocalDateTime.of(2024, 1, 1, 10, 0, 0)
        );
        jdbcTemplate.update(
                "INSERT INTO DR_POINT (USER_NUMBER, POINT_NOTE, POINT_CHANGE, POINT_DATE) VALUES (?, ?, ?, ?)",
                TEST_USER_NUMBER, "리뷰 작성 포인트", 150, LocalDateTime.of(2024, 1, 2, 11, 0, 0)
        );
    }

    @Test
    @DisplayName("내 정보 확인 테스트")
    void getUserInfo() {
        // given - @BeforeEach에서 TEST_USER_NUMBER (1L) 사용자가 준비됨

        // when
        UserInfoDTO userInfoDTO = myPageMapper.getUserInfo(TEST_USER_NUMBER);

        // then
        assertNotNull(userInfoDTO, "사용자 정보가 조회되어야 합니다.");
        assertEquals(TEST_USER_NICKNAME, userInfoDTO.getUserNickName(), "닉네임 검증");
        assertEquals(TEST_USER_EMAIL, userInfoDTO.getUserEmail(), "이메일 검증");
        assertEquals(TEST_USER_PHONE, userInfoDTO.getUserPhone(), "전화번호 검증");
        // assertEquals(615, userInfoDTO.getEnvironmentScore(), "환경 점수 검증"); // UserInfoDTO에 해당 필드가 있고, DB에 컬럼이 있다면
        // assertEquals(1, userInfoDTO.getEnvironmentRank(), "환경 순위 검증");   // UserInfoDTO에 해당 필드가 있고, DB에 컬럼이 있다면
        // assertEquals(40, userInfoDTO.getTotalPoints(), "총 포인트 검증");     // UserInfoDTO에 해당 필드가 있고, DB에 컬럼이 있다면
        assertEquals(TEST_PROFILE_PIC, userInfoDTO.getProfilePic(), "사진 경로 검증"); // UserInfoDTO의 getPhoto()가 PROFILE_PIC을 반환한다고 가정

        log.info("조회된 사용자 정보: {}", userInfoDTO);
    }

    @Test
    @DisplayName("닉네임 중복 확인 테스트")
    void checkNickname() {
        // given
        String existingNickname = TEST_USER_NICKNAME; // @BeforeEach에서 삽입한 닉네임

        // when
        int existingNicknameCount = myPageMapper.checkNickname(existingNickname);

        // then
        assertEquals(1, existingNicknameCount, "기존 닉네임('" + existingNickname + "')과 중복 (1이 나와야 함)");
        log.info("기존 닉네임 중복 여부 ({}): {}", existingNickname, existingNicknameCount);

        // 존재하지 않는 닉네임 테스트
        String nonExistingNickname = "이닉네임은정말로없을거예요123";
        int nonExistingNicknameCount = myPageMapper.checkNickname(nonExistingNickname);
        assertEquals(0, nonExistingNicknameCount, "존재하지 않는 닉네임은 0이 나와야 함");
        log.info("존재하지 않는 닉네임 중복 여부 ({}): {}", nonExistingNickname, nonExistingNicknameCount);
    }

    @Test
    @DisplayName("닉네임 업데이트 테스트")
    void updateNickname() {
        // given
        String newNickname = "새로운테스트닉네임";

        // 새로운 닉네임이 DB에 없는지 먼저 확인 (선택 사항)
        assertEquals(0, myPageMapper.checkNickname(newNickname), "테스트 시작 시 새로운 닉네임은 존재하지 않아야 함");

        // when
        int updatedRows = myPageMapper.updateNickname(TEST_USER_NUMBER, newNickname);

        // then
        assertEquals(1, updatedRows, "닉네임 업데이트는 1행에 영향을 주어야 함");
        UserInfoDTO userInfoDTOAfter = myPageMapper.getUserInfo(TEST_USER_NUMBER);
        assertNotNull(userInfoDTOAfter, "업데이트 후 사용자 정보가 조회되어야 합니다.");
        assertEquals(newNickname, userInfoDTOAfter.getUserNickName(), "닉네임이 새로운 닉네임으로 업데이트되어야 함");
        log.info("업데이트 전 닉네임: {}, 업데이트 후 닉네임: {}", TEST_USER_NICKNAME, userInfoDTOAfter.getUserNickName());
    }


    @Test
    @DisplayName("회원 탈퇴 테스트")
    void deleteUser() {
        // given - @BeforeEach에서 TEST_USER_NUMBER (1L) 사용자가 준비됨

        // when
        int deletedRows = myPageMapper.deleteUser(TEST_USER_NUMBER);

        // then
        assertEquals(1, deletedRows, "회원 탈퇴는 1행에 영향을 주어야 함");
        UserInfoDTO userInfoDTOAfter = myPageMapper.getUserInfo(TEST_USER_NUMBER);
        assertNull(userInfoDTOAfter, "사용자가 삭제되어 조회되지 않아야 합니다.");
        log.info("회원 탈퇴 처리 후, 유저 정보 (기대값 null): {}", userInfoDTOAfter);
    }

    @Test
    @DisplayName("포인트 내역 조회 테스트")
    void pointHistory() {
        // given - @BeforeEach에서 TEST_USER_NUMBER (1L) 사용자에 대한 포인트 내역 2건이 준비됨

        // when
        List<PointDetailDTO> actualPointHistory = myPageMapper.pointHistory(TEST_USER_NUMBER);

        // then
        assertNotNull(actualPointHistory, "포인트 내역 리스트는 null이 아니어야 합니다.");
        assertEquals(2, actualPointHistory.size(), "포인트 내역의 항목 수가 2개여야 합니다.");

        // SQL 쿼리의 ORDER BY DP.POINT_DATE DESC, DP.POINT_NUMBER DESC 에 따라 정렬된 순서대로 검증
        // @BeforeEach에서 삽입한 데이터와 일치하는지 확인

        // 첫 번째로 나올 내역 (가장 최근: 2024-01-02)
        PointDetailDTO latestEntry = actualPointHistory.get(0);
        assertEquals("리뷰 작성 포인트", latestEntry.getPointNote());
        assertEquals(150, latestEntry.getPointGet());
        // 날짜 포맷은 SQL에서 DATE_FORMAT으로 '%Y-%m-%d %H:%i:%s' 처리했으므로, DTO의 getFormattedPointDate()가 이 형식으로 반환해야 함
        // 또는 DTO의 pointDate 필드가 LocalDateTime 이라면 직접 비교하거나 포맷팅하여 비교
        // assertEquals("2024-01-02 11:00:00", latestEntry.getFormattedPointDate()); // DTO에 getFormattedPointDate()가 있다고 가정
        // totalPoints는 윈도우 함수로 계산되므로, DB와 쿼리 결과를 보고 정확한 기대값을 설정해야 함
        // 1. 2024-01-01: +200 (누적 200)
        // 2. 2024-01-02: +150 (누적 350)
        // ORDER BY POINT_DATE ASC로 누적합을 구했으므로, 2024-01-02 데이터의 totalPoints는 350
        assertEquals(350, latestEntry.getTotalPoints(), "가장 최근 내역의 누적 포인트 검증");


        // 두 번째로 나올 내역 (그 다음: 2024-01-01)
        PointDetailDTO olderEntry = actualPointHistory.get(1);
        assertEquals("출석 포인트", olderEntry.getPointNote());
        assertEquals(200, olderEntry.getPointGet());
        // assertEquals("2024-01-01 10:00:00", olderEntry.getFormattedPointDate());
        // 2024-01-01 데이터의 totalPoints는 200
        assertEquals(200, olderEntry.getTotalPoints(), "이전 내역의 누적 포인트 검증");


        log.info("조회된 포인트 내역: {}", actualPointHistory);
    }
}