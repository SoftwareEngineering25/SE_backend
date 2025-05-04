package com.dr.dto.recipe;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MyRecipeListDTO {
    private Long recipeNumber;              // 레시피 번호 (ID 대신 사용)
    private String recipeTitle;             // 레시피 제목
    private String photoOriginal;           // 원본 사진 경로
    private String photoLocal;              // 로컬 사진 경로
    private String userNickName;            // 작성자
    private String recipeWriteDate;         // 작성일
    private Long goodCount;                  // 추천수
}
