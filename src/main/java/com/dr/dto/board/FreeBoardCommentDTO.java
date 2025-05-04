package com.dr.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FreeBoardCommentDTO {
    private Long replyNumber;
    private String replyText;
    private String replyWriteDate;
    private String replyModifyDate;
    private String photoLocal;
    private Long userNumber;
    private Long boardNumber;
    private String userNickName;
    private String profilePic;
}
