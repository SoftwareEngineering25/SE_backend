package com.dr.dto.manager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ManagerRecipeDTO {
    private int recipeNumber;
    private String recipeTitle;
    private String userNickName;
    private int goodCount;
    private String recipeWriteDate;
    private String recipeType;
}
