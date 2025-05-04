package com.dr.service.chatBot;

import com.dr.dto.chatBot.NangjangbotDTO;
import com.dr.mapper.chatBot.NangjangbotMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional //메서드나 클래스에 트랜잭션을 적용하여 데이터베이스 작업의 원자성을 보장하는 데 사용
// 트랜잭션은 여러 데이터베이스 작업을 하나의 단위로 묶어 처리하며,
// 이 중 하나라도 실패하면 전체 작업이 취소되어 데이터 무결성을 유지
@RequiredArgsConstructor //final 필드의 생성자를 자동생성
public class NangjangbotService {
    private final NangjangbotMapper nangjangbotMapper;

    // 최초입력시 채팅방 생성
    public void createChatSession(NangjangbotDTO nangjangbotDTO) {
        nangjangbotMapper.createChatSession(nangjangbotDTO);
    }

    // 유저의 가장 최근 채팅방 조회
    public Long getSessionNumber(Long userNumber){
        return nangjangbotMapper.getSessionNumber(userNumber);
    }

    // 유저 질문 저장
    public void insertUserMsg(NangjangbotDTO nangjangbotDTO) {
        nangjangbotMapper.insertUserMsg(nangjangbotDTO);
    }

    // 챗봇 응답 저장
    public void insertBotReply(NangjangbotDTO nangjangbotDTO){
        nangjangbotMapper.insertBotReply(nangjangbotDTO);
    }

    // 이전채팅 불러오기
    public List<NangjangbotDTO> getChatList(Long userNumber) {
        return nangjangbotMapper.getChatList(userNumber);
    }

    // 채팅삭제
    public void deleteChat(Long sessionNumber){
        nangjangbotMapper.deleteChat(sessionNumber);
    };

    // 이전채팅내용 조회
    public List<NangjangbotDTO> getChatContents(Long sessionNumber){
        return nangjangbotMapper.getChatContents(sessionNumber);
    }
}
