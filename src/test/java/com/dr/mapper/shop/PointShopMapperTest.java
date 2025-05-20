package com.dr.mapper.shop;

import com.dr.dto.shop.PointShopDTO;
// ... 다른 import ...
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class PointShopMapperTest {

    @Autowired
    PointShopMapper pointShopMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final Long TEST_USER_NUMBER = 7L;
    private static final String TEST_USER_PHONE = "01077777777";
    private static final String PRODUCT_NAME_HOTSIX = "핫식스";
    private static final int PRODUCT_PRICE_HOTSIX = 1000;
    private static final String PRODUCT_CODE_HOTSIX = "HOTSIX_CODE_UNIQUE_1"; // 상품당 유니크 코드
    private static final String PRODUCT_NAME_COKE = "코카콜라";
    private static final int PRODUCT_PRICE_COKE = 1500;
    private static final String PRODUCT_CODE_COKE = "COKE_CODE_UNIQUE_1";   // 상품당 유니크 코드
    private static final Long INITIAL_USER_POINTS = 5000L;



    @BeforeEach
    void setUp() {
        // 데이터 정리 (외래키 제약 고려)
        jdbcTemplate.update("DELETE FROM DR_POINT WHERE USER_NUMBER = ?", TEST_USER_NUMBER);
        jdbcTemplate.update("DELETE FROM DR_PRODUCT WHERE PRODUCT_NAME = ? OR PRODUCT_NAME = ?", PRODUCT_NAME_HOTSIX, PRODUCT_NAME_COKE);
        jdbcTemplate.update("DELETE FROM DR_USER WHERE USER_NUMBER = ?", TEST_USER_NUMBER);

        // 테스트 사용자 삽입
        jdbcTemplate.update(
                "INSERT INTO DR_USER (USER_NUMBER, USER_EMAIL, USER_PW, USER_NICKNAME, USER_PHONE, USER_STATUS, PROFILE_PIC) " + // TOTAL_POINTS 컬럼이 있다고 가정
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                TEST_USER_NUMBER, "user7@example.com", "pw7", "테스트유저7", TEST_USER_PHONE, "일반회원", "profile7.png"
        );

        // 테스트 상품 삽입
        jdbcTemplate.update(
                "INSERT INTO DR_PRODUCT (PRODUCT_NAME, PRODUCT_CODE, PRODUCT_PRICE) VALUES (?, ?, ?)",
                PRODUCT_NAME_HOTSIX, PRODUCT_CODE_HOTSIX, PRODUCT_PRICE_HOTSIX
        );
        jdbcTemplate.update(
                "INSERT INTO DR_PRODUCT (PRODUCT_NAME, PRODUCT_CODE, PRODUCT_PRICE) VALUES (?, ?, ?)",
                PRODUCT_NAME_COKE, PRODUCT_CODE_COKE, PRODUCT_PRICE_COKE
        );
        // @BeforeEach setUp() 메소드 내부, DR_USER 삽입 후
        jdbcTemplate.update(
                "INSERT INTO DR_POINT (USER_NUMBER, POINT_NOTE, POINT_CHANGE, POINT_DATE) VALUES (?, ?, ?, NOW())",
                TEST_USER_NUMBER, "테스트 초기 포인트", INITIAL_USER_POINTS // INITIAL_USER_POINTS는 5000L
        );
    }

    @Test
    @DisplayName("사용자의 현재 포인트 조회 테스트")
    void testGetMyPoint() {
        // when
        Long totalPoint = pointShopMapper.getMyPoint(TEST_USER_NUMBER);

        // then
        assertThat(totalPoint)
                .isNotNull()
                .isEqualTo(INITIAL_USER_POINTS);
        log.info("사용자 {}의 현재 포인트: {}", TEST_USER_NUMBER, totalPoint);
    }

    @Test
    @DisplayName("모든 상품 목록 조회 테스트")
    void testSelectAllProduct() {
        // when
        List<PointShopDTO> products = pointShopMapper.selectAllProduct();

        // then
        assertThat(products)
                .isNotEmpty()
                .hasSize(2)
                .extracting(PointShopDTO::getProductName)
                .containsExactlyInAnyOrder(PRODUCT_NAME_HOTSIX, PRODUCT_NAME_COKE);
        log.info("조회된 전체 상품 수: {}", products.size());
    }

    // PointShopMapperTest.java (testGetProductCode 수정)
    @Test
    @DisplayName("상품명으로 상품 코드 조회 테스트 (상품당 단일 코드 가정)")
    void testGetProductCode() {
        // given
        PointShopDTO pointShopDTO = new PointShopDTO();
        pointShopDTO.setProductName(PRODUCT_NAME_HOTSIX);
        pointShopDTO.setQuantity(1L); // LIMIT 절에 사용할 값이므로 null이 아니어야 함

        // when
        List<String> resultProductCodes = pointShopMapper.getProductCode(pointShopDTO);

        // then
        assertThat(resultProductCodes)
                .isNotNull()
                .hasSize(1) // LIMIT 1 이므로 결과는 1개
                .containsExactly(PRODUCT_CODE_HOTSIX);
        log.info("{} 상품 코드 조회 결과: {}", PRODUCT_NAME_HOTSIX, resultProductCodes);
    }

    @Test
    @DisplayName("사용자 전화번호 조회 테스트")
    void testGetUserPhone() {
        // when
        String phone = pointShopMapper.getUserPhone(TEST_USER_NUMBER);

        // then
        assertThat(phone)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(TEST_USER_PHONE);
        log.info("사용자 {}의 전화번호: {}", TEST_USER_NUMBER, phone);
    }

    @Test
    @DisplayName("사용자 포인트 사용 내역 기록 테스트")
    void testInsertUserPoint() {
        // given
        PointShopDTO pointShopDTO = new PointShopDTO();
        pointShopDTO.setPointNote("상품 구매: " + PRODUCT_NAME_HOTSIX);

        Long cost = 1000L;
        pointShopDTO.setTotalCost(cost); // DTO 필드명에 맞게
        pointShopDTO.setUserNumber(TEST_USER_NUMBER);

        // when
        // insertUserPoint가 DR_POINT에 기록하고, 영향을 받은 행 수를 반환한다고 가정
        // (DR_USER의 TOTAL_POINTS 업데이트는 서비스 로직에서 별도 처리)
        int insertedRows = pointShopMapper.insertUserPoint(pointShopDTO);

        // then
        assertEquals(1, insertedRows, "포인트 사용 내역이 1행 삽입되어야 함");

        // (선택적) DB에서 직접 확인하여 검증 강화
        // Map<String, Object> pointEntry = jdbcTemplate.queryForMap("SELECT POINT_CHANGE, POINT_NOTE FROM DR_POINT WHERE USER_NUMBER = ? ORDER BY POINT_NUMBER DESC LIMIT 1", TEST_USER_NUMBER);
        // assertThat(pointEntry.get("POINT_CHANGE")).isEqualTo(-cost); // 포인트 차감은 음수
        // assertThat(pointEntry.get("POINT_NOTE")).isEqualTo("상품 구매: " + PRODUCT_NAME_HOTSIX);

        log.info("포인트 사용 내역 삽입 테스트 완료");
    }
    // PointShopMapperTest.java (testDeleteCode 수정)
    @Test
    @DisplayName("상품 코드 삭제 테스트 (수량 지정)")
    void testDeleteCode() {
        // given
        PointShopDTO pointShopDTO = new PointShopDTO();
        pointShopDTO.setProductName(PRODUCT_NAME_HOTSIX);
        pointShopDTO.setQuantity(1L); // 삭제할 코드의 수 (LIMIT에 사용될 값)

        // 삭제 전 코드 존재 확인 (선택적이지만 권장)
        String codeBefore = jdbcTemplate.queryForObject(
                "SELECT PRODUCT_CODE FROM DR_PRODUCT WHERE PRODUCT_NAME = ? AND PRODUCT_CODE = ?",
                String.class,
                PRODUCT_NAME_HOTSIX, PRODUCT_CODE_HOTSIX // @BeforeEach에서 설정한 특정 코드
        );
        assertThat(codeBefore).isEqualTo(PRODUCT_CODE_HOTSIX);

        // when
        int deletedRows = pointShopMapper.deleteCode(pointShopDTO);

        // then
        // LIMIT 1로 하나의 PRODUCT_CODE를 가진 레코드를 삭제했으므로 deletedRows는 1이 되어야 함
        assertEquals(1, deletedRows, "상품 레코드가 1개 삭제되어야 합니다.");

        // 삭제 후 해당 PRODUCT_CODE를 가진 레코드가 없는지 확인
        List<String> codesAfter = jdbcTemplate.queryForList(
                "SELECT PRODUCT_CODE FROM DR_PRODUCT WHERE PRODUCT_NAME = ? AND PRODUCT_CODE = ?",
                String.class,
                PRODUCT_NAME_HOTSIX, PRODUCT_CODE_HOTSIX
        );
        assertThat(codesAfter).isEmpty();

        log.info("{} 상품 코드({}) 삭제 테스트 완료", PRODUCT_NAME_HOTSIX, PRODUCT_CODE_HOTSIX);
    }
}