package com.dr.service.manager;

import com.dr.dto.manager.*;
import com.dr.mapper.manager.ManagerMapper;
import com.dr.dto.manager.ManagerUserDTO; // 필요한 DTO import

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerServiceTest {

    private ManagerService managerService; // 서비스 클래스

    @Mock
    private ManagerMapper managerMapper; // Mapper 인터페이스

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // @Mock으로 선언된 필드(managerMapper)를 초기화
        managerService = new ManagerService(managerMapper); // 생성자를 통해 Mock 객체(managerMapper)를 주입하여 managerService 인스턴스 생성
    }

    // 1. 관리자 로그인
    @Test
    public void managerLogin() {
        // given
        ManagerSessionDTO dto = new ManagerSessionDTO();
        dto.setManagerName("송아성");
        doReturn(Optional.of(dto)).when(managerMapper).managerLogin(any(), any());

        // when
        Optional<ManagerSessionDTO> result = Optional.ofNullable(managerService.managerLogin("manager1@dr.com", "password1!"));

        // then
        assertThat(result).isPresent(); // 결과가 존재하는지 확인
        assertThat(result.get().getManagerName()).isEqualTo("송아성"); // DTO의 managerName과 비교
    }

    // 2. 대시보드
    @Test
    public void managerDashBoard() {
        // given
        DashBoardDTO expectedDashBoardDTO = new DashBoardDTO();
        expectedDashBoardDTO.setUserAll(60); // 예상 사용자 수
        expectedDashBoardDTO.setNumAll(27); // 예상 총 수

        ManagerDTO expectedManagerDTO = new ManagerDTO();
        expectedManagerDTO.setManagerName("송아성"); // 예상 관리자 이름

        // Mocking
        doReturn(expectedDashBoardDTO).when(managerMapper).dashBoardInfo(); // 대시보드 정보 Mocking
        doReturn(List.of(expectedManagerDTO)).when(managerMapper).managerInfo(); // 관리자 정보 Mocking

        // when
        DashBoardDTO result = managerService.dashBoardInfo(); // 서비스 메서드 호출
        List<ManagerDTO> managerList = managerService.managerInfo(); // 관리자 정보 가져오기

        // then
        assertNotNull(result); // 결과가 null이 아님을 확인
        assertEquals(expectedDashBoardDTO.getUserAll(), result.getUserAll()); // 사용자 수 비교

        // 관리자 정보 비교
        ManagerDTO actualManagerDTO = managerList.get(0); // 0번째 인덱스 가져오기
        assertEquals(expectedManagerDTO.getManagerName(), actualManagerDTO.getManagerName()); // 관리자 이름 비교
    }
    // 3. 회원 관리
    @Test
    public void manageUser() {
        ManagerUserDTO managerUserDTO = new ManagerUserDTO();
        // managerUserDTO.setUserEmail("manager1@dr.com"); // 필요하다면 설정

        List<ManagerUserDTO> expectedList = List.of(managerUserDTO);
        // Mocking 설정: managerMapper의 manageUser()가 호출되면 expectedList를 반환하도록
        doReturn(expectedList).when(managerMapper).manageUser();

        // when: 서비스 메소드 호출
        List<ManagerUserDTO> userLists = managerService.manageUser();

        // then
        // 1. managerMapper의 manageUser() 메소드가 정확히 1번 호출되었는지 검증
        verify(managerMapper, times(1)).manageUser();

        // 2. 반환된 리스트가 null이 아니고, 기대한 내용을 포함하는지 검증
        assertNotNull(userLists);
        assertFalse(userLists.isEmpty(), "사용자 목록은 비어있지 않아야 합니다.");
        assertEquals(1, userLists.size(), "반환된 리스트의 크기는 1이어야 합니다.");
        assertSame(managerUserDTO, userLists.get(0), "반환된 객체는 Mocking 시 사용한 객체와 동일해야 합니다.");
    }

    // 4. 게시글 관리 (삭제)
    @Test
    public void manageBoard() {
        Integer boardNumber = 1; // 삭제할 게시글 번호
        when(managerMapper.boardDelete(boardNumber)).thenReturn(true); // Mock 동작 정의

        // 삭제 메소드 실행
        boolean result = managerService.boardDelete(boardNumber);

        // 결과 검증
        assertTrue(result);
        verify(managerMapper, times(1)).boardDelete(boardNumber); // 메소드 호출 검증
    }

    // 5. 레시피 관리 (삭제)
    @Test
    public void recipeDeleteTest() {
        Integer recipeNumber = 1; // 삭제할 레시피 번호
        when(managerMapper.recipeDelete(recipeNumber)).thenReturn(true); // Mock 동작 정의

        // 삭제 메소드 실행
        boolean result = managerService.recipeDelete(recipeNumber);

        // 결과 검증
        assertTrue(result);
        verify(managerMapper, times(1)).recipeDelete(recipeNumber);
    }

    // 6. 댓글 관리 (조회)
    @Test
    public void showReplyTest() {
        // 테스트 데이터 준비
        ManagerCommentDTO comment1 = new ManagerCommentDTO(1, "답변 내용 1", "닉네임1", "010-1234-5678", "2024-01-01", 1, 1, "게시판 타입", "레시피 타입");
        ManagerCommentDTO comment2 = new ManagerCommentDTO(2, "답변 내용 2", "닉네임2", "010-2345-6789", "2024-01-02", 2, 2, "게시판 타입", "레시피 타입");
        List<ManagerCommentDTO> mockReplies = Arrays.asList(comment1, comment2); // Mock 데이터 리스트

        when(managerMapper.showReply()).thenReturn(mockReplies); // Mock 동작 정의

        // 조회 메소드 실행
        List<ManagerCommentDTO> result = managerService.showReply();

        // 결과 검증
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(comment1, result.get(0));
        assertEquals(comment2, result.get(1));
        verify(managerMapper, times(1)).showReply();
    }

    // 7. 포인트 (회수)
    @Test
    public void takePointTest() {
        Integer pointNumber = 1; // 회수할 포인트 번호
        when(managerMapper.takePoint(pointNumber)).thenReturn(true); // Mock 동작 정의

        // 포인트 회수 메소드 실행
        boolean result = managerService.takePoint(pointNumber);

        // 결과 검증
        assertTrue(result);
        verify(managerMapper, times(1)).takePoint(pointNumber);
    }

    // 8. 신고 관리 (삭제)
    @Test
    public void reportDeleteTest() {
        Integer sirenNumber = 1; // 삭제할 신고 번호
        when(managerMapper.reportDelete(sirenNumber)).thenReturn(true); // Mock 동작 정의

        // 신고 삭제 메소드 실행
        boolean result = managerService.reportDelete(sirenNumber);

        // 결과 검증
        assertTrue(result);
        verify(managerMapper, times(1)).reportDelete(sirenNumber);
    }


    // 9. 상품 관리 (삭제)
    @Test
    public void productDeleteTest() {
        String productName = "테스트 상품"; // 삭제할 상품 이름

        // Mock 동작 정의
        when(managerMapper.productDelete(productName)).thenReturn(true);

        // 삭제 메소드 실행
        boolean result = managerService.productDelete(productName);

        // 결과 검증
        assertTrue(result); // 삭제가 성공적으로 이루어졌는지 확인

        // 메소드가 호출되었는지 검증
        verify(managerMapper, times(1)).productDelete(productName);
    }
}
