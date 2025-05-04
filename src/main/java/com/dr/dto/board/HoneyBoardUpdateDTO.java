package com.dr.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HoneyBoardUpdateDTO {
    private Long boardNumber;
    private String boardTitle;
    private String boardText;
    private String boardModifyDate;
    private String photoOriginal;
    private String photoLocal;
    private String photoSize;
    private String photoUpload;
}
