package com.dr.dto.manager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ManagerBoardDTO {
    private int boardNumber;
    private String boardTitle;
    private String userNickName;
    private int goodCount;
    private String boardWriteDate;
    private String boardType;
}
