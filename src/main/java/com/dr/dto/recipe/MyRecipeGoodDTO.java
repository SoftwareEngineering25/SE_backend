package com.dr.dto.recipe;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MyRecipeGoodDTO {
    private Long goodNumber;
    private Long recipeNumber;
    private Long userNumber;
}