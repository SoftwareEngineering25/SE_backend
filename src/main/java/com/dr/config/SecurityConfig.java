package com.dr.config;

import com.dr.domain.CustomOAuth2User;
import com.dr.dto.user.KakaoUsersDTO;
import com.dr.dto.user.UserSessionDTO;
import com.dr.service.user.CustomOAuth2UserService;
import com.dr.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId; // application.yml에서 클라이언트 ID 값을 주입받음

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    // 카카오로 로그아웃을 넘겨버림으로써 완전히 로그아웃 가능하도록 구현.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        // --- authorizeHttpRequests 블록 수정 ---
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/board/freeBoardWriteOk").authenticated()
                .requestMatchers("/board/freeBoardWrite").authenticated() // 예시: GET 요청으로 폼 페이지 접근 시
                .anyRequest().permitAll()
        );


        http.oauth2Login(oauth2Login -> oauth2Login
                .loginPage("/user/login")
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(this.customOAuth2UserService)
                )
                .successHandler(this.successHandler()) // this.successHandler() 호출
        ).logout(logout -> logout
                .logoutSuccessHandler((request, response, authentication) -> {
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.invalidate(); // 세션 무효화
                    }
                    // 카카오 로그아웃 URL로 리다이렉트
                    String logoutRedirectUri = "http://localhost:8888/main";
                    String kakaoLogoutUrl = "https://kauth.kakao.com/oauth/logout?client_id=" +
                            kakaoClientId + "&logout_redirect_uri=" + logoutRedirectUri;
                    response.sendRedirect(kakaoLogoutUrl);
                })
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "loginId") // 적절한 쿠키 삭제
                .clearAuthentication(true)
        );
        return http.build();
    }


    // 로그인 성공 핸들러
    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            // 세션에 추가정보 담기 위해 객체생성
            HttpSession session = request.getSession();

            CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            // 필요한 사용자 정보 가져오기
            String name = customOAuth2User.getName();
            String profilePic = customOAuth2User.getProfilePic();
            String providerId = customOAuth2User.getProviderId();
            String provider = customOAuth2User.getProvider();
            String accountEmail = customOAuth2User.getAccountEmail();
            Long userNumber = customOAuth2User.getUserNumber();

            // 세션에 사용자 정보 저장
            session.setAttribute("providerId", providerId);
            session.setAttribute("provider", provider);
            session.setAttribute("userNickName", name);
            session.setAttribute("accountEmail", accountEmail);
            session.setAttribute("profilePic", profilePic);

            if (Boolean.TRUE.equals(session.getAttribute("isNewUser"))) {
                // 새로운 사용자라면 추가 정보 입력 페이지로 리다이렉트
                response.sendRedirect("/user/apiJoin");
            } else {
                // 기존 사용자라면 메인 페이지로 이동
                session.setAttribute("userNumber", userNumber);
                session.getAttribute("providerId");
                response.sendRedirect("/DRmain");
            }
        };
    }
}

