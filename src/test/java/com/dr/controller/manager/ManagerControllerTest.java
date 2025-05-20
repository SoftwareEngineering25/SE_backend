package com.dr.controller.manager;

import com.dr.dto.manager.DashBoardDTO;
import com.dr.dto.manager.ManagerDTO;
import com.dr.service.manager.ManagerService; // ★ ManagerService import 추가
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import com.dr.mybatis.MyBatisConfig;
import com.dr.config.SecurityConfig;
import com.dr.service.user.CustomOAuth2UserService;
import com.dr.mapper.user.KakaoUsersMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean; // ★ MockBean import 추가
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view; // ★ view import 추가
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model; // ★ model import 추가
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@WebMvcTest(ManagerController.class)
@Import({SecurityConfig.class, MyBatisConfig.class, CustomOAuth2UserService.class})
@ActiveProfiles("local")
public class ManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean // ★ ManagerService Mock Bean 추가
    private ManagerService managerService;

    @MockBean // ★ KakaoUsersMapper Mock Bean 추가
    private KakaoUsersMapper kakaoUsersMapper; // ★ KakaoUsersMapper의 실제 import 경로 수정 필요


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(post("/manager/managerLogin")
                        .param("managerEmail", "manager1@dr.com")
                        .param("managerPw", "password1!")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    // 2.대시보드
    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"}) // ★ @WithMockUser 다시 추가 (managerDashBoard는 인증 필요)
    void managerDashBoard() throws Exception {
        // given
        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setManagerName("송아성"); // 예시로 이름 설정
        List<ManagerDTO> managerList = Arrays.asList(managerDTO); // 리스트로 감싸기

        DashBoardDTO dashBoardDTO = new DashBoardDTO();
        dashBoardDTO.setUserAll(60); // 예시 데이터 설정
        dashBoardDTO.setNumAll(27);

        // Mocking: Mock 객체인 managerService의 메소드 호출 시 반환값 설정
        when(managerService.managerInfo()).thenReturn(managerList);
        when(managerService.dashBoardInfo()).thenReturn(dashBoardDTO);

        // when & then
        mockMvc.perform(get("/manager/dashBoard")) // ★ .with(user(...)) 대신 @WithMockUser 사용 (managerDashBoard는 이 방식이 더 깔끔)
                .andExpect(status().isOk()) // 상태 코드가 200인지 확인
                .andExpect(view().name("manager/dashBoard")) // 뷰 이름 확인
                .andExpect(model().attributeExists("manager")) // manager가 모델에 존재하는지 확인
                .andExpect(model().attributeExists("dashBoard")); // dashBoard가 모델에 존재하는지 확인
    }



}
