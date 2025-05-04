package com.dr.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FreeBoardDetailDTO {
    private Long boardNumber;
    private String boardTitle;
    private String boardType;
    private String boardText;
    private String boardWriteDate;
    private String replyModifyDate;
    private String userNickName;
    private Long userNumber;
    private Long goodCount;
    private String photoLocal;
    private String profilePic;
}
