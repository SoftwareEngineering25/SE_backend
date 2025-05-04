package com.dr.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FreeBoardListDTO {
    private Long boardNumber;
    private String boardTitle;
    private String userNickName;
    private Long goodCount;
    private String boardWriteDate;
}
