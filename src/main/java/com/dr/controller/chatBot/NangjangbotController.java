package com.dr.controller.chatBot;

import com.dr.dto.chatBot.NangjangbotDTO;
import com.dr.service.chatBot.NangjangbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chatBot") //최상위 경로
public class NangjangbotController {
    private final NangjangbotService nangjangbotService;

    @GetMapping("/nangjangbot") //하위경로
    public String openNangjangbot(Model model, @SessionAttribute(value = "userNumber", required = false) Long userNumber) {

        //로그인하지 않았을 경우 처리
        if (userNumber == null) {
            return "redirect:/user/login"; // 로그인 페이지로 리다이렉션
        }

        //처음 입장시엔 채팅방 번호가 없고, 첫 채팅 입력시 DB를 넣기 때문에 0으로 초기화 후 바로 DTO의 session필드 넘기기
        NangjangbotDTO nangjangbotDTO = new NangjangbotDTO();
        nangjangbotDTO.setSessionNumber(0L);

        //이전 채팅방 목록 불러오기
        List<NangjangbotDTO> chatList = nangjangbotService.getChatList(userNumber);

        //모델에 담아 뷰로 전송
        model.addAttribute("nangjangbotDTO", nangjangbotDTO);
        model.addAttribute("chatList", chatList);

        return "chatBot/nangjangbot";
    }


}
