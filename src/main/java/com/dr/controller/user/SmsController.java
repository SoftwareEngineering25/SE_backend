package com.dr.controller.user;

import com.dr.service.user.CoolSmsService;
import jakarta.servlet.http.HttpSession;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private static final int CODE_EXPIRATION_MINUTES = 5; // 인증번호 유효 시간 (5분)

    @Autowired
    private CoolSmsService coolSmsService;

    // 인증번호 전송
    @PostMapping("/send")
    public String sendSms(@RequestBody Map<String, String> body, HttpSession session) {
        String phoneNumber = body.get("phoneNumber");
        try {
            String generatedCode = coolSmsService.sendSms(phoneNumber);

            // 세션에 인증번호와 생성 시간 저장
            session.setAttribute("authCode", generatedCode);
            session.setAttribute("authCodeCreatedTime", LocalDateTime.now());
            return "인증코드가 발송되었습니다.";
        } catch (CoolsmsException e) {
            e.printStackTrace();
            return "SMS 전송 오류: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "예외 발생: " + e.getMessage();
        }
    }

    @PostMapping("/verify")
    public String verifyCode(@RequestBody Map<String, String> body, HttpSession session) {
        String inputCode = body.get("authCode");
        String sessionCode = (String) session.getAttribute("authCode");
        LocalDateTime codeCreatedTime = (LocalDateTime) session.getAttribute("authCodeCreatedTime");

        if (sessionCode != null && codeCreatedTime != null) {
            LocalDateTime currentTime = LocalDateTime.now();
            if (currentTime.isBefore(codeCreatedTime.plusMinutes(CODE_EXPIRATION_MINUTES))) {
                if (sessionCode.equals(inputCode)) {
                    session.setAttribute("authCodeVerified", "verified"); // 인증 성공 상태 저장
                    session.removeAttribute("authCode");
                    session.removeAttribute("authCodeCreatedTime");
                    return "인증이 완료되었습니다.";
                } else {
                    return "인증번호가 일치하지 않습니다.";
                }
            } else {
                session.removeAttribute("authCode");
                session.removeAttribute("authCodeCreatedTime");
                return "인증번호가 만료되었습니다. 다시 시도해 주세요.";
            }
        }
        return "인증번호를 요청해주세요.";
    }

}
