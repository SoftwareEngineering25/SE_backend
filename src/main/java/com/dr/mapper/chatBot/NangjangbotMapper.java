package com.dr.mapper.chatBot;

import com.dr.dto.chatBot.NangjangbotDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NangjangbotMapper {
    // 최초입력시 채팅방 생성
    void createChatSession(NangjangbotDTO nangjangbotDTO);

    // 유저의 가장 최근 채팅방 조회
    Long getSessionNumber(Long userNumber);

    // 유저 질문 저장
    void insertUserMsg(NangjangbotDTO nangjangbotDTO);

    // 챗봇 응답 저장
    void insertBotReply(NangjangbotDTO nangjangbotDTO);

    // 이전채팅 불러오기
    List<NangjangbotDTO> getChatList(Long userNumber);

    // 채팅삭제
    void deleteChat(Long sessionNumber);

    // 이전채팅내용 조회
    List<NangjangbotDTO> getChatContents(Long sessionNumber);
}
