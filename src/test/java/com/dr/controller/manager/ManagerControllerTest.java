package com.dr.controller.manager;

import com.dr.dto.manager.DashBoardDTO;
import com.dr.dto.manager.ManagerDTO;
import com.dr.dto.manager.ManagerSessionDTO;
import com.dr.service.manager.ManagerService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManagerController.class)  // 컨트롤러 명시
class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManagerService managerService;

    @MockBean
    private HttpSession session;

    // 1. 관리자 로그인
    @Test
    void testLogin() throws Exception {

        ManagerSessionDTO mockManagerDTO = new ManagerSessionDTO();
        mockManagerDTO.setManagerName("Manager Name");

        when(managerService.managerLogin(any(), any())).thenReturn(mockManagerDTO);

        mockMvc.perform(post("/manager/managerLogin")
                        .param("managerEmail", "manager1@dr.com")
                        .param("managerPw", "password1!"))
                .andExpect(status().is3xxRedirection()) // 리다이렉션 상태 확인
                .andExpect(redirectedUrl("/manager/dashBoard")); // 리다이렉션 URL 확인
    }

    // 2.대시보드
    @Test
    void managerDashBoard() throws Exception {
        // given
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setManagerName("송아성"); // 예시로 이름 설정
        List<ManagerDTO> managerList = Arrays.asList(managerDTO); // 리스트로 감싸기

        DashBoardDTO dashBoardDTO = new DashBoardDTO();
        dashBoardDTO.setUserAll(60); // 예시 데이터 설정
        dashBoardDTO.setNumAll(27);

        // Mocking
        when(managerService.managerInfo()).thenReturn(managerList);
        when(managerService.dashBoardInfo()).thenReturn(dashBoardDTO);

        // when & then
        mockMvc.perform(get("/manager/dashBoard"))
                .andExpect(status().isOk()) // 상태 코드가 200인지 확인
                .andExpect(view().name("manager/dashBoard")) // 뷰 이름 확인
                .andExpect(model().attributeExists("manager")) // manager가 모델에 존재하는지 확인
                .andExpect(model().attributeExists("dashBoard")); // dashBoard가 모델에 존재하는지 확인
    }



}
