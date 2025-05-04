package com.dr.dto.recipe;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MyRecipeUpdateDTO {
    private Long recipeNumber;
    private String recipeTitle;
    private String recipeText;
    private String recipeModifyDate;
    private String photoOriginal;
    private String photoLocal;
    private String photoSize;
    private String photoUpload;

}
