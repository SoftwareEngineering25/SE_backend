package com.dr.mapper.manager;
import com.dr.dto.manager.ManagerRegisterDTO;
import org.apache.ibatis.annotations.Mapper;
import com.dr.dto.manager.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional; // Optional import 추가
import java.util.stream.Collectors; // stream 사용 시 필요

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.jdbc.Sql; // import 필요
import org.springframework.test.context.TestPropertySource; // ★ import 추가

@Transactional
@Slf4j
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/SE?serverTimezone=UTC&characterEncoding=UTF-8", // ★ 실제 DB URL로 변경
        "spring.datasource.username=kimseonmin", // ★ 실제 DB 사용자 이름으로 변경
        "spring.datasource.password=1234", // ★ 실제 DB 비밀번호로 변경
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "mybatis.type-aliases-package=com.dr.dto" // ★ 실제 DTO 패키지 경로에 맞게 변경
})
class ManagerMapperTest {

    @Autowired
    ManagerMapper managerMapper;

    // DTO 필드는 테스트 메서드 내에서 선언하고 사용하는 것이 더 명확합니다.
    // ManagerSessionDTO managerSessionDTO;
    // DashBoardDTO dashBoardDTO;
    // ManagerUserDTO managerUserDTO;

    @BeforeEach
    void setUp() {
        // managerSessionDTO = new ManagerSessionDTO();
        // managerSessionDTO.setManagerName("송아성");
        // managerSessionDTO.setManagerEmail("manager1@dr.com");
        // managerSessionDTO.setManagerPw("password1!");
        // BeforeEach에서 DTO를 초기화하는 것은 현재 테스트 로직에 직접 사용되지 않으므로 불필요합니다.
        // 실제 DB에 데이터를 삽입하는 것은 @Sql 어노테이션으로 처리합니다.
    }

    // 1.관리자 로그인
    @Test
    @Sql("classpath:sql/manager/test-manager-login.sql")
    void managerLogin() {
        // given: test-manager-login.sql 파일에 manager1@dr.com 계정 정보 삽입

        // when
        // ★ Optional<ManagerDTO> -> Optional<ManagerSessionDTO> 로 타입 변경
        Optional<ManagerSessionDTO> result = managerMapper.managerLogin("manager1@dr.com", "password1!");

        // then
        assertTrue(result.isPresent(), "관리자 계정이 존재해야 합니다.");
        // ★ ManagerDTO -> ManagerSessionDTO 로 타입 변경
        ManagerSessionDTO manager = result.get();
        assertEquals("송아성", manager.getManagerName(), "관리자 이름이 일치해야 합니다.");
        // System.out.println(manager.getManagerName());
    }

    // 2. 대시보드
    @Test
    @Sql("classpath:sql/manager/test-dashboard.sql") // ★ 테스트 데이터 삽입 SQL 파일 지정
    void dashBoard() {
        // given: test-dashboard.sql 파일에 대시보드 정보 계산에 필요한 데이터 삽입 (예: 사용자 수, 게시글 수 등)

        // when
        DashBoardDTO dashBoardDTO = managerMapper.dashBoardInfo();
        List<ManagerDTO> managerList = managerMapper.managerInfo();

        // then
        assertNotNull(dashBoardDTO, "대시보드 정보는 null이 아니어야 합니다.");
        assertNotNull(managerList, "관리자 목록은 null이 아니어야 합니다.");
        assertFalse(managerList.isEmpty(), "관리자 목록은 비어있지 않아야 합니다."); // 목록이 비어있지 않음을 확인

        // 예상되는 전체 사용자 수와 게시글 수로 변경 (test-dashboard.sql에 삽입된 데이터에 따라 달라짐)
        assertEquals(60, dashBoardDTO.getUserAll(), "전체 사용자 수가 일치해야 합니다.");
        assertEquals(3, dashBoardDTO.getNumAll(), "전체 게시글 수가 일치해야 합니다.");

        // 0번째 인덱스 접근 전 리스트 크기 확인 또는 반복문 사용 권장
        // 여기서는 test-dashboard.sql에 최소 1명의 관리자가 있다고 가정
        ManagerDTO managerDTO = managerList.get(0);
        assertEquals("송아성", managerDTO.getManagerName(), "첫 번째 관리자 이름이 일치해야 합니다."); // 예상 이름으로 변경
    }

    //3. 회원 관리 (조회)
    @Test
    @Sql("classpath:sql/manager/test-manage-user.sql") // ★ 테스트 데이터 삽입 SQL 파일 지정
    void manageUser(){
        // given: test-manage-user.sql 파일에 여러 사용자 데이터 삽입 (최소 4명)

        // when
        List<ManagerUserDTO> managerUserList = managerMapper.manageUser();

        // then
        assertNotNull(managerUserList, "회원 목록은 null이 아니어야 합니다.");
        assertFalse(managerUserList.isEmpty(), "회원 목록은 비어있지 않아야 합니다.");
        assertTrue(managerUserList.size() > 3, "회원 목록은 최소 4명 이상이어야 합니다 (get(3) 접근)."); // get(3) 접근을 위한 검증

        ManagerUserDTO managerUserDTO = managerUserList.get(4); // 4번째 사용자 정보 가져오기
        assertNotNull(managerUserDTO, "4번째 사용자 정보는 null이 아니어야 합니다.");

        // test-manage-user.sql에 삽입된 4번째 사용자의 닉네임으로 변경
        assertEquals("닉네임4" , managerUserDTO.getUserNickname(), "4번째 사용자 닉네임이 일치해야 합니다.");
    }

    //4.게시글 관리 (삭제)
    @Test
    @Sql("classpath:sql/manager/test-board-delete.sql") // ★ 테스트 데이터 삽입 SQL 파일 지정 (삭제할 게시글 포함)
    void board() {
        // given: test-board-delete.sql 파일에 게시글 데이터 삽입 (삭제할 게시글 번호 82 포함)

        // when
        managerMapper.boardDelete(82); // 게시글 번호 82 삭제
        List<ManagerBoardDTO> managerBoardList = managerMapper.showBoard(); // 삭제 후 목록 다시 조회

        // then
        assertNotNull(managerBoardList, "게시물 목록은 null이 아니어야 합니다.");

        // 삭제된 게시물(82번)이 목록에 없는지 확인
        boolean isDeletedBoardPresent = managerBoardList.stream()
                .anyMatch(dto -> dto.getBoardNumber() == 82);
        assertFalse(isDeletedBoardPresent, "삭제된 게시물(82번)이 목록에 포함되어 있지 않아야 합니다.");

        // 만약 삭제 후 목록이 비어있을 것으로 예상한다면 추가 검증
        // assertTrue(managerBoardList.isEmpty(), "게시물 목록이 비어 있어야 합니다."); // 필요에 따라 추가
    }

    // 5. 레시피 관리 (삭제)
    @Test
    @Sql("classpath:sql/manager/test-recipe-delete.sql") // ★ 테스트 데이터 삽입 SQL 파일 지정 (삭제할 레시피 포함)
    void recipe() {
        // given: test-recipe-delete.sql 파일에 레시피 데이터 삽입 (삭제할 레시피 번호 31 포함)

        // when
        managerMapper.recipeDelete(31); // 레시피 번호 31 삭제
        List<ManagerRecipeDTO> managerRecipeList = managerMapper.showRecipe(); // 삭제 후 목록 다시 조회

        // then
        assertNotNull(managerRecipeList, "레시피 목록은 null이 아니어야 합니다.");

        // 삭제된 레시피(31번)가 목록에 없는지 확인
        boolean isDeletedRecipePresent = managerRecipeList.stream()
                .anyMatch(dto -> dto.getRecipeNumber() == 31);
        assertFalse(isDeletedRecipePresent, "삭제된 레시피(31번)가 목록에 포함되어 있지 않아야 합니다.");

        // 만약 삭제 후 목록이 비어있을 것으로 예상한다면 추가 검증
        // assertTrue(managerRecipeList.isEmpty(), "레시피 목록이 비어 있어야 합니다."); // 필요에 따라 추가
    }


    // 6. 댓글 관리 (조회)
    @Test
    @Sql("classpath:sql/manager/test-manage-reply.sql") // ★ 테스트 데이터 삽입 SQL 파일 지정
    void manageReply(){
        // given: test-manage-reply.sql 파일에 댓글 데이터 삽입 (최소 1개)

        // when
        List<ManagerCommentDTO> managerReplyList = managerMapper.showReply();

        // then
        assertNotNull(managerReplyList, "댓글 목록은 null이 아니어야 합니다.");
        assertFalse(managerReplyList.isEmpty(), "댓글 목록은 비어있지 않아야 합니다."); // 목록이 비어있지 않음을 확인

        ManagerCommentDTO managerCommentDTO = managerReplyList.get(0); // 첫 번째 댓글 정보 가져오기
        assertNotNull(managerCommentDTO, "첫 번째 댓글 정보는 null이 아니어야 합니다.");

        // test-manage-reply.sql에 삽입된 첫 번째 댓글의 내용으로 변경
        assertEquals("UniqueText_127" , managerCommentDTO.getReplyText(), "첫 번째 댓글 내용이 일치해야 합니다.");
    }


    // 7. 포인트 (회수)
    @Test
    @Sql("classpath:sql/manager/test-point.sql") // ★ 테스트 데이터 삽입 SQL 파일 지정 (회수할 포인트 포함)
    void point() {
        // given: test-point.sql 파일에 포인트 데이터 삽입 (회수할 포인트 번호 140 포함)
        int pointNumberToTake = 140; // 회수할 포인트 번호

        // when: 특정 포인트 번호에 대해 POINT_CHANGE를 0으로 설정하는 쿼리 실행
        managerMapper.takePoint(pointNumberToTake);

        // then: 변경 후 목록 다시 가져와서 확인
        List<ManagerPointDTO> updatedPointList = managerMapper.showPoint();

        assertNotNull(updatedPointList, "포인트 목록은 null이 아니어야 합니다.");
        assertFalse(updatedPointList.isEmpty(), "포인트 목록은 비어있지 않아야 합니다.");

        // 회수한 포인트 항목을 찾아서 POINT_CHANGE 값이 0인지 확인
        Optional<ManagerPointDTO> updatedPointDTO = updatedPointList.stream()
                .filter(dto -> dto.getPointNumber() == pointNumberToTake)
                .findFirst();

        assertTrue(updatedPointDTO.isPresent(), "해당 포인트 항목(" + pointNumberToTake + ")이 목록에 존재해야 합니다.");
        assertEquals(0, updatedPointDTO.get().getPointGet(), "회수된 포인트 항목의 POINT_GET 값이 0이어야 합니다."); // POINT_GET 필드 이름 확인 필요
    }


    // 8. 신고 관리 (삭제)
    @Test
    @Sql("classpath:sql/manager/test-report-delete.sql") // ★ 테스트 데이터 삽입 SQL 파일 지정 (삭제할 신고 포함)
    void report() {
        // given: test-report-delete.sql 파일에 신고 데이터 삽입 (삭제할 신고 번호 1 포함)
        int sirenNumberToDelete = 1; // 삭제할 신고 항목의 ID

        // when: 특정 신고 항목 삭제
        managerMapper.reportDelete(sirenNumberToDelete);

        // then: 변경 후 목록 다시 가져와서 확인
        List<ManagerReportDTO> updatedReportList = managerMapper.showReport();

        assertNotNull(updatedReportList, "신고 목록은 null이 아니어야 합니다.");

        // 삭제된 항목이 목록에 없는지 확인
        boolean isDeletedReportPresent = updatedReportList.stream()
                .anyMatch(dto -> dto.getSirenNumber() == sirenNumberToDelete);

        assertFalse(isDeletedReportPresent, "삭제된 신고 항목(" + sirenNumberToDelete + ")이 목록에 포함되어 있지 않아야 합니다.");

        // 만약 삭제 후 목록이 비어있을 것으로 예상한다면 추가 검증
        // assertTrue(updatedReportList.isEmpty(), "신고 목록이 비어 있어야 합니다."); // 필요에 따라 추가
    }

    // 9. 상품 관리 (등록)
    @Test
    // @Sql("classpath:sql/manager/test-register-product.sql") // 등록 전 필요한 데이터가 있다면 추가
    void registerProduct() {
        // given: 등록할 상품 정보를 세팅
        ManagerRegisterDTO newProduct = new ManagerRegisterDTO();
        newProduct.setProductName("테스트 상품");
        newProduct.setProductCode("TEST123");
        newProduct.setProductPrice(10000);

        // when: 상품 등록 메서드 호출
        // Mapper 인터페이스의 반환 타입을 int로 변경했다면 이 코드는 그대로 둡니다.
        int insertedRows = managerMapper.registerProduct(newProduct);

        // then: 삽입된 행의 개수가 1인지 확인
        assertEquals(1, insertedRows, "상품이 성공적으로 등록되어야 합니다 (1행 삽입).");

        // 만약 useGeneratedKeys를 사용한다면, 생성된 키가 DTO에 설정되었는지 확인
        // assertNotNull(newProduct.getProductNumber(), "등록된 상품의 번호가 설정되어야 합니다.");

        // DTO 필드 값 확인 (이 부분은 Mocking 없이도 가능하지만, 현재 로직 유지)
        assertEquals("테스트 상품", newProduct.getProductName(), "상품명이 일치해야 합니다.");
        assertEquals("TEST123", newProduct.getProductCode(), "상품 코드가 일치해야 합니다.");
        assertEquals(10000, newProduct.getProductPrice(), "상품 가격이 일치해야 합니다.");
    }
}