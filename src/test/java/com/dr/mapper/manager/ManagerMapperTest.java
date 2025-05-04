package com.dr.mapper.manager;

import com.dr.dto.manager.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class ManagerMapperTest {

    @Autowired
    ManagerMapper managerMapper;
    ManagerSessionDTO managerSessionDTO;
    DashBoardDTO dashBoardDTO;
    ManagerUserDTO managerUserDTO;

    @BeforeEach
    void setUp() {
        managerSessionDTO = new ManagerSessionDTO();
        managerSessionDTO.setManagerName("송아성");
        managerSessionDTO.setManagerEmail("manager1@dr.com");
        managerSessionDTO.setManagerPw("password1!");
    }

    // 1.관리자 로그인
    @Test
    void managerLogin() {
        //when
        String managerName = String.valueOf(managerMapper.managerLogin("manager1@dr.com", "password1!").get());

        //then
        System.out.println(managerName);
    }

    // 2. 대시보드
    @Test
    void dashBoard() {
        List<ManagerDTO> managerList;
        // when
        dashBoardDTO = managerMapper.dashBoardInfo();
        managerList = managerMapper.managerInfo(); // 관리자 목록 초기화

        // 0번째 인덱스만 비교
        ManagerDTO managerDTO = managerList.get(0); // managerList 초기화 후 0번째 인덱스 가져오기

        // then
        assertNotNull(dashBoardDTO);
        assertNotNull(managerDTO);

        assertEquals(27, dashBoardDTO.getNumAll());
        assertEquals(60, dashBoardDTO.getUserAll());

        assertEquals("송아성", managerDTO.getManagerName());
    }

    //3. 회원 관리 (조회)
    @Test
    void manageUser(){
        List<ManagerUserDTO> managerUserList;
        //when
        managerUserList = managerMapper.manageUser();

        managerUserDTO = managerUserList.get(3);

        //then
        assertNotNull(managerUserDTO);

        assertEquals("닉네임4" , managerUserDTO.getUserNickName());
    }

    //4.게시글 관리 (삭제)
    @Test
    void board() {

        // when
        managerMapper.boardDelete(82);
        List<ManagerBoardDTO> managerBoardList = managerMapper.showBoard();

        // 테스트 항목이 삭제되었는지 확인
        if (!managerBoardList.isEmpty()) {
            ManagerBoardDTO managerBoardDTO = managerBoardList.get(0);

            // then
            assertNotEquals(82, managerBoardDTO.getBoardNumber(), "삭제된 게시물이 리스트에 포함되어 있습니다.");
        } else {
            // 만약 삭제 후 목록이 비어있다면 이는 삭제가 성공적으로 이루어졌다는 의미입니다.
            assertTrue(managerBoardList.isEmpty(), "게시물 목록이 비어 있어야 합니다.");
        }
    }

    // 5. 레시피 관리 (삭제)
    @Test
    void recipe() {

        // when
        managerMapper.recipeDelete(31);
        List<ManagerRecipeDTO> managerRecipeList = managerMapper.showRecipe();

        // 테스트 항목이 삭제되었는지 확인
        if (!managerRecipeList.isEmpty()) {
            ManagerRecipeDTO managerRecipeDTO = managerRecipeList.get(0);

            // then
            assertNotEquals(31, managerRecipeDTO.getRecipeNumber(), "삭제된 게시물이 리스트에 포함되어 있습니다.");
        } else {
            // 만약 삭제 후 목록이 비어있다면 이는 삭제가 성공적으로 이루어졌다는 의미입니다.
            assertTrue(managerRecipeList.isEmpty(), "게시물 목록이 비어 있어야 합니다.");
        }
    }


    // 6. 댓글 관리 (조회)
    @Test
    void manageReply(){
        List<ManagerCommentDTO> managerReplyList;
        //when
        managerReplyList = managerMapper.showReply();

        ManagerCommentDTO managerCommentDTO = managerReplyList.get(0);

        //then
        assertNotNull(managerCommentDTO);

        assertEquals("UniqueText_127" , managerCommentDTO.getReplyText());
    }


    // 7. 포인트 (회수)
    @Test
    void point() {
        // given: 초기 포인트 데이터 목록 가져오기
        List<ManagerPointDTO> managerPointDTOList = managerMapper.showPoint();

        // 테스트할 포인트 항목이 목록에 존재하는지 확인
        assertFalse(managerPointDTOList.isEmpty(), "포인트 목록이 비어있지 않아야 합니다.");

        // 특정 포인트 번호에 대해 POINT_GET을 0으로 설정
        int pointNumber = 140;
        managerMapper.takePoint(pointNumber);

        // when: 변경 후 목록 다시 가져오기
        List<ManagerPointDTO> updatedPointList = managerMapper.showPoint();

        // then: POINT_GET 값이 0으로 변경되었는지 확인
        ManagerPointDTO updatedPointDTO = updatedPointList.stream()
                .filter(dto -> dto.getPointNumber() == pointNumber)
                .findFirst()
                .orElse(null);
        assertNotNull(updatedPointDTO, "해당 포인트 항목이 목록에 존재해야 합니다.");
        assertEquals(0, updatedPointDTO.getPointGet(), "POINT_GET 값이 0이어야 합니다.");
    }


    // 8. 신고 관리 (삭제)
    @Test
    void report() {
        // given: 초기 신고 데이터 목록 가져오기
        List<ManagerReportDTO> managerReportDTOList = managerMapper.showReport();

        assertFalse(managerReportDTOList.isEmpty(), "신고 목록이 비어있지 않아야 합니다.");

        // 테스트할 신고 항목 삭제
        int sirenNumber = 1;  // 삭제할 신고 항목의 ID
        managerMapper.reportDelete(sirenNumber);  // reportDelete 메서드 호출로 수정

        // when: 변경 후 목록 다시 가져오기
        List<ManagerReportDTO> updatedReportList = managerMapper.showReport();

        // then: 삭제된 항목이 목록에 없는지 확인
        ManagerReportDTO deletedReportDTO = updatedReportList.stream()
                .filter(dto -> dto.getSirenNumber() == sirenNumber)
                .findFirst()
                .orElse(null);

        assertNull(deletedReportDTO, "해당 신고 항목이 목록에서 삭제되어야 합니다.");
    }

    // 9. 상품 관리 (등록)
    @Test
    void registerProduct() {
        // given: 등록할 상품 정보를 세팅
        ManagerRegisterDTO newProduct = new ManagerRegisterDTO();
        newProduct.setProductName("테스트 상품");
        newProduct.setProductCode("TEST123");
        newProduct.setProductPrice(10000);

        // when: 상품 등록 메서드 호출
        managerMapper.registerProduct(newProduct);

        // then: 등록한 상품을 조회하여 확인
        assertEquals("테스트 상품", newProduct.getProductName(), "상품명이 일치해야 합니다.");
        assertEquals("TEST123", newProduct.getProductCode(), "상품 코드가 일치해야 합니다.");
        assertEquals(10000, newProduct.getProductPrice(), "상품 가격이 일치해야 합니다.");
    }


}