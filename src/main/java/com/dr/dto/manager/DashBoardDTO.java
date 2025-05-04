package com.dr.dto.manager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DashBoardDTO {
    private int userAll;
    private int numAll;
    private int boardNum;
    private int recipeNum;
    private int replyNum;
    private String today;

}
