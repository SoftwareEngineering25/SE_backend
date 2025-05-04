package com.dr.dto.recipe;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChatBotRecipeWriteDTO {
    private Long recipeNumber;
    private Long userNumber;
    private String recipeTitle;
    private String recipeText;
    private String userNickName;
    private String recipeWriteDate;
    private String recipeModifyDate;
    private String recipeType;
}
