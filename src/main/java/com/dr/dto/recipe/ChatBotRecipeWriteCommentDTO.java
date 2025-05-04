package com.dr.dto.recipe;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class ChatBotRecipeWriteCommentDTO {
    private Long userNumber;       // 댓글 작성자 ID
    private Long recipeNumber;     // 레시피 ID (어떤 레시피에 작성되는지)
    private String replyText;      // 댓글 내용
    private Long replyNumber;      // 댓글 번호
}
