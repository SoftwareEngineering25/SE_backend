package com.dr.service.board;

import com.dr.dto.board.*;
import com.dr.mapper.board.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;

    // 최신순으로 자유게시판 리스트 가져오기
    public List<FreeBoardListDTO> freeBoardList() {
        return boardMapper.freeBoardList();
    }

    // 추천순으로 자유게시판 리스트 가져오기
    public List<FreeBoardListDTO> freeBoardListGood() {
        return boardMapper.freeBoardListGood();
    }


    // 자유게시판 상세 조회
    public FreeBoardDetailDTO freeBoardDetail(Long boardNumber) {
        return boardMapper.freeBoardDetail(boardNumber);
    }

    // 자유게시판 댓글 목록 조회
    public List<FreeBoardCommentDTO> freeBoardCommentList(Long boardNumber) {
        return boardMapper.freeBoardCommentList(boardNumber);
    }

    // 자유게시판 댓글작성
    public void freeBoardInsertReply(FreeBoardCommentDTO freeBoardCommentDTO) {
        boardMapper.freeBoardInsertReply(freeBoardCommentDTO);
    }

    // 자유게시판 댓글수정      // 서비스 계층 메서드에서 replyText를 인자로 받도록 수정
    public void freeBoardUpdateReply(Long replyNumber, String replyText) {
        // 댓글 객체 생성 및 데이터 설정
        FreeBoardCommentDTO comment = new FreeBoardCommentDTO();
        comment.setReplyNumber(replyNumber);
        comment.setReplyText(replyText);

        // 업데이트 호출
        boardMapper.freeBoardUpdateReply(comment);
    }

    // 자유게시판 댓글 삭제
    public void freeBoardDeleteReply(Long replyNumber) {
        boardMapper.freeBoardDeleteReply(replyNumber);
    }

    // 자유게시판 추천 플러스
    public void freeGoodPlus(FreeGoodDTO freeGoodDTO) {
        boardMapper.freeGoodPlus(freeGoodDTO);
    }

    // 자유게시판 추천 마이너스
    public void freeGoodMinus(FreeGoodDTO freeGoodDTO) {
        boardMapper.freeGoodMinus(freeGoodDTO);
    }

    // 최신순으로 꿀팁게시판 리스트 가져오기
    public List<HoneyBoardListDTO> honeyBoardList() {
        return boardMapper.honeyBoardList();
    }

    // 추천순으로 꿀팁게시판 리스트 가져오기
    public List<HoneyBoardListDTO> honeyBoardListGood() {
        return boardMapper.honeyBoardListGood();
    }

    // 꿀팁게시판 상세 조회
    public HoneyBoardDetailDTO honeyBoardDetail(Long boardNumber) {
        return boardMapper.honeyBoardDetail(boardNumber);
    }

    // 꿀팁게시판 댓글 목록 조회
    public List<HoneyBoardCommentDTO> honeyBoardCommentList(Long boardNumber) {
        return boardMapper.honeyBoardCommentList(boardNumber);
    }

    // 꿀팁게시판 댓글작성
    public void honeyBoardInsertReply(HoneyBoardCommentDTO honeyBoardCommentDTO) {
        boardMapper.honeyBoardInsertReply(honeyBoardCommentDTO);
    }

    // 꿀팁게시판 댓글수정      // 서비스 계층 메서드에서 replyText를 인자로 받도록 수정
    public void honeyBoardUpdateReply(Long replyNumber, String replyText) {
        // 댓글 객체 생성 및 데이터 설정
        HoneyBoardCommentDTO comment = new HoneyBoardCommentDTO();
        comment.setReplyNumber(replyNumber);
        comment.setReplyText(replyText);

        // 업데이트 호출
        boardMapper.honeyBoardUpdateReply(comment);
    }

    // 꿀팁게시판 댓글 삭제
    public void honeyBoardDeleteReply(Long replyNumber) {
        boardMapper.honeyBoardDeleteReply(replyNumber);
    }


    // 꿀팁게시판 추천 플러스
    public void honeyGoodPlus(HoneyGoodDTO honeyGoodDTO) {
        boardMapper.honeyGoodPlus(honeyGoodDTO);
    }

    // 꿀팁게시판 추천 마이너스
    public void honeyGoodMinus(HoneyGoodDTO honeyGoodDTO) {
        boardMapper.honeyGoodMinus(honeyGoodDTO);
    }

    // 게시판 신고
    public void report(BoardReportDTO reportDTO) {
        boardMapper.report(reportDTO);
    }

    //자유게시판 게시글 작성
    @Transactional
    public void saveFreeBoard(FreeBoardWriteDTO freeBoardWriteDTO, FreeBoardPhotoDTO freeBoardPhotoDTO) {
        //1. 게시글 내용 저장 (boardText에 이미지 URL이 포함됨)
        boardMapper.freeBoardInsertWrite(freeBoardWriteDTO);  // 게시글 본문 저장

        // 2. BOARD_NUMBER와 USER_NUMBER를 설정하여 FreeBoardPhotoDTO에 추가
        Long boardNumber = freeBoardWriteDTO.getBoardNumber();
        Long userNumber = freeBoardWriteDTO.getUserNumber();

        freeBoardPhotoDTO.setBoardNumber(boardNumber);   //BOARD_NUMBER 설정
        freeBoardPhotoDTO.setUserNumber(userNumber);    //USER_ NUMBER 설정

        //3. 사진 정보 저장(PHOTO 테이블)
        boardMapper.freeBoardInsertPhoto(freeBoardPhotoDTO);
    }

    //자유게시판 게시글 삭제
    @Transactional
    public void freeBoardDeleteWriteAndPhoto(Long boardNumber) {
        boardMapper.freeBoardDeleteWrite(boardNumber);
        boardMapper.freeBoardDeletePhoto(boardNumber);
    }

    // 자유게시판 게시글 수정
    @Transactional
    public void freeBoardUpdateWriteAndPhoto(FreeBoardUpdateDTO freeBoardUpdateDTO) {
        // 게시글 제목, 내용 수정
        boardMapper.freeBoardUpdateWrite(freeBoardUpdateDTO);

        // 사진 정보 수정
        boardMapper.freeBoardUpdatePhoto(freeBoardUpdateDTO);
    }

    //꿀팁게시판 게시글 작성
    @Transactional
    public void saveHoneyBoard(HoneyBoardWriteDTO honeyBoardWriteDTO, HoneyBoardPhotoDTO honeyBoardPhotoDTO) {
        //1. 게시글 내용 저장 (boardText에 이미지 URL이 포함됨)
        boardMapper.honeyBoardInsertWrite(honeyBoardWriteDTO);  // 게시글 본문 저장

        // 2. BOARD_NUMBER와 USER_NUMBER를 설정하여 FreeBoardPhotoDTO에 추가
        Long boardNumber = honeyBoardWriteDTO.getBoardNumber();
        Long userNumber = honeyBoardWriteDTO.getUserNumber();

        honeyBoardPhotoDTO.setBoardNumber(boardNumber);   //BOARD_NUMBER 설정
        honeyBoardPhotoDTO.setUserNumber(userNumber);    //USER_ NUMBER 설정

        //3. 사진 정보 저장(PHOTO 테이블)
        boardMapper.honeyBoardInsertPhoto(honeyBoardPhotoDTO);
    }

    //꿀팁게시판 게시글 삭제
    @Transactional
    public void honeyBoardDeleteWriteAndPhoto(Long boardNumber) {
        boardMapper.honeyBoardDeleteWrite(boardNumber);
        boardMapper.honeyBoardDeletePhoto(boardNumber);
    }

    //꿀팁게시판 게시글 수정
    @Transactional
    public void honeyBoardUpdateWriteAndPhoto(HoneyBoardUpdateDTO honeyBoardUpdateDTO) {
        boardMapper.honeyBoardUpdateWrite(honeyBoardUpdateDTO);
        boardMapper.honeyBoardUpdatePhoto(honeyBoardUpdateDTO);
    }

}




