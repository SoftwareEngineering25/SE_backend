package com.dr.dto.myPage;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SirenListDTO {
    private Long sirenNumber;         // 순번
    private String titleAndContent;   // 제목 및 내용
    private String sirenType;         // 유형
    private String sirenReason;       // 신고 사유
    private String sirenDate;         // 신고 날짜
    private String boardNumber;       // 게시판 번호
    private String replyNumber;       // 댓글 번호
}
