package com.dr.mapper.board;

import com.dr.dto.board.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {

    //자유게시판 최신순
    List<FreeBoardListDTO> freeBoardList();

    //자유게시판 추천순
    List<FreeBoardListDTO> freeBoardListGood();


    // 자유게시판 상세페이지 조회 메서드
    FreeBoardDetailDTO freeBoardDetail(@Param("boardNumber") Long boardNumber);

    // 자유게시판 댓글 목록 조회 메서드
    List<FreeBoardCommentDTO> freeBoardCommentList(@Param("boardNumber") Long boardNumber);


    //  자유게시판 댓글 작성
    void freeBoardInsertReply(FreeBoardCommentDTO freeBoardCommentDTO);


    // 자유게시판 댓글 수정
    void freeBoardUpdateReply(FreeBoardCommentDTO comment);

    // 자유게시판 댓글 삭제
    void freeBoardDeleteReply(Long replyNumber);

    // 꿀팁게시판 추천 플러스
    void freeGoodPlus(FreeGoodDTO FreeGoodDTO);

    // 꿀팁게시판 추천 마이너스
    void freeGoodMinus(FreeGoodDTO FreeGoodDTO);



    //꿀팁 게시판 최신순
    List<HoneyBoardListDTO> honeyBoardList();

    //꿀팁 게시판 추천순
    List<HoneyBoardListDTO> honeyBoardListGood();

    // 꿀팁게시판 상세페이지 조회 메서드
    HoneyBoardDetailDTO honeyBoardDetail(@Param("boardNumber") Long boardNumber);

    // 꿀팁게시판 댓글 목록 조회 메서드
    List<HoneyBoardCommentDTO> honeyBoardCommentList(@Param("boardNumber") Long boardNumber);

    //  꿀팁게시판 댓글 작성
    void honeyBoardInsertReply(HoneyBoardCommentDTO honeyBoardCommentDTO);


    // 꿀팁게시판 댓글 수정
    void honeyBoardUpdateReply(HoneyBoardCommentDTO comment);

    // 꿀팁게시판 댓글 삭제
    void honeyBoardDeleteReply(Long replyNumber);

    // 꿀팁게시판 추천 플러스
    void honeyGoodPlus(HoneyGoodDTO HoneyGoodDTO);

    // 꿀팁게시판 추천 마이너스
    void honeyGoodMinus(HoneyGoodDTO HoneyGoodDTO);

    // 게시판 신고
    void report(BoardReportDTO boardReportDTO);

    // 자유게시판 게시글 작성
    void freeBoardInsertWrite(FreeBoardWriteDTO freeBoardWriteDTO);

    // 자유게시판 게시글 사진 등록
    void freeBoardInsertPhoto(FreeBoardPhotoDTO freeBoardPhotoDTO);

    //자유게시판 게시글 삭제
    void freeBoardDeleteWrite(Long boardNumber);

    //자유게시판 게시글 사진 삭제
    void freeBoardDeletePhoto(Long boardNumber);

    //자유게시판 게시글 수정
    void freeBoardUpdateWrite(FreeBoardUpdateDTO freeBoardUpdateDTO);

    //자유게시판 게시글 사진 수정
    void freeBoardUpdatePhoto(FreeBoardUpdateDTO freeBoardUpdateDTO);

    // 꿀팁게시판 게시글 작성
    void honeyBoardInsertWrite(HoneyBoardWriteDTO honeyBoardWriteDTO);

    // 꿀팁게시판 게시글 사진 등록
    void honeyBoardInsertPhoto(HoneyBoardPhotoDTO honeyBoardPhotoDTO);

    // 꿀팁게시판 게시글 삭제
    void honeyBoardDeleteWrite(Long boardNumber);

    // 꿀팁게시판 게시글 사진 삭제
    void honeyBoardDeletePhoto(Long boardNumber);

    //꿀팁게시판 게시글 수정
    void honeyBoardUpdateWrite(HoneyBoardUpdateDTO honeyBoardUpdateDTO);

    //꿀팁게시판 게시글 사진 수정
    void honeyBoardUpdatePhoto(HoneyBoardUpdateDTO honeyBoardUpdateDTO);

}
