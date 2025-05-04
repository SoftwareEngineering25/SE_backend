package com.dr.dto.main;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SearchDTO {
    //검색 테이블
    private String recipeTitle;
    private String boardTitle;
    private String userNickName;
    private String photoLocal;
    private Long goodCount;
    private String boardWriteDate;
    private String recipeWriteDate;
    private String searchType;
    private String searchValue;
    private Long boardNumber;
    private Long recipeNumber;
    private String boardType;
    private String recipeType;
}
