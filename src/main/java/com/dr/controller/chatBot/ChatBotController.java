package com.dr.controller.chatBot;

import com.dr.dto.chatBot.ChatRequest;
import com.dr.dto.chatBot.ChatResponse;
import com.dr.dto.chatBot.NangjangbotDTO;
import com.dr.service.chatBot.ChatBotService;
import com.dr.service.chatBot.NangjangbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatbot")
public class ChatBotController {

    private final ChatBotService chatBotService;
    private final NangjangbotService nangjangbotService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request,
                                             @SessionAttribute(value = "userNumber", required = false) Long userNumber) {
        //로그인하지 않았을 경우 처리
        if (userNumber == null) {
            //401 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 메세지와 현재 세션 가져오기
        String userMessage = request.getMessage();
        Long sessionNumber = request.getSessionNumber();

        // 가져온 세션 Service 메소드 이용하기 위해 DTO에 저장
        NangjangbotDTO nangjangbotDTO = new NangjangbotDTO();
        nangjangbotDTO.setUserNumber(userNumber);
        nangjangbotDTO.setSessionTitle(userMessage);

        // 현재 채팅방 번호가 0이라면 채팅방 생성하여 sessionNumber 생성후 조회
        if(sessionNumber == 0L){
            // 채팅방 생성메소드
            nangjangbotService.createChatSession(nangjangbotDTO);
            // 방금 생성한 채팅방 조회하여 변수에 저장
            sessionNumber = nangjangbotService.getSessionNumber(userNumber);
        }

        // 사이드바에 뿌릴 정보 초기화
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setSessionNumber(sessionNumber);
        chatResponse.setSessionTitle(userMessage);
        chatResponse.setCreateDate("방금 전");

        // 채팅방에 유저 질문 저장
        nangjangbotDTO.setUserNumber(userNumber);
        nangjangbotDTO.setSessionNumber(sessionNumber);
        nangjangbotDTO.setUserMsg(userMessage);
        nangjangbotService.insertUserMsg(nangjangbotDTO);

        // api 응답
        try {
            chatResponse.setReply(chatBotService.getChatbotResponse(userMessage));
//            botReply = chatBotService.getChatbotResponse(userMessage);
            System.out.println("지피티 답변 : " + chatResponse.getReply());

            // 채팅방에 챗봇 응답 저장 (세션번호와 유저번호는 위에서 저장했으므로 여기서는 botReply만 셋팅)
            nangjangbotDTO.setBotReply(chatResponse.getReply());
            nangjangbotService.insertBotReply(nangjangbotDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(chatResponse);
        }

        // 생성된,현재 유지중인 세션 번호와 세션타이틀, 생성날짜, 챗봇 메세지 chatResponse에 담아 함께 반환
        return ResponseEntity.ok(chatResponse);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteChat(@RequestBody NangjangbotDTO nangjangbotDTO,
                                           @SessionAttribute(value = "userNumber", required = false) Long userNumber) {
        //로그인하지 않았을 경우 처리
        if (userNumber == null) {
            //401 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //채팅 삭제메소드
        Long sessionNumber = nangjangbotDTO.getSessionNumber();
        nangjangbotService.deleteChat(sessionNumber);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/getChating")
    public ResponseEntity<List<NangjangbotDTO>> getChating(@RequestBody NangjangbotDTO nangjangbotDTO,
                                                           @SessionAttribute(value = "userNumber", required = false) Long userNumber) {
        //로그인하지 않았을 경우 처리
        if (userNumber == null) {
            // 401 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //세션번호를 채팅내용 조회 메소드에 적용하여 호출
        Long sessionNumber = nangjangbotDTO.getSessionNumber();
        List<NangjangbotDTO> chatList = nangjangbotService.getChatContents(sessionNumber);

        return ResponseEntity.ok(chatList);
    }
}
