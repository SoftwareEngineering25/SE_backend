package com.dr.dto.recipe;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChatBotRecipeDetailDTO {
    private Long recipeNumber;           // 레시피 번호 (ID 대신 사용)
    private String recipeTitle;          // 레시피 제목
    private String recipeText;           // 레시피 내용
    private String photoLocal;           // 이미지 경로
    private String userNickName;         // 작성자
    private String recipeWriteDate;      // 작성일
    private String recipeModifyDate;     // 수정일
    private Long goodCount;               // 추천수
    private Long userNumber;
    private String recipeType;
    private String profilePic;
}
