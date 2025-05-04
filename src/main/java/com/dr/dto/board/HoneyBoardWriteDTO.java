package com.dr.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HoneyBoardWriteDTO {
    private Long boardNumber;
    private Long userNumber;
    private String boardTitle;
    private String boardText;
    private String photoOriginal;
    private String photoLocal;
    private String userNickName;
    private String boardWriteDate;
    private String boardModifyDate;
    private String boardType;
    private Long photoNumber;
}
