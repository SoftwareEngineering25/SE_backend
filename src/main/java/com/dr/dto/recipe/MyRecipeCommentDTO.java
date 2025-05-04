package com.dr.dto.recipe;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MyRecipeCommentDTO {
    private Long replyNumber;               //댓글번호
    private String replyText;               //댓글내용
    private String replyWriteDate;         // 댓글작성일
    private String replyModifyDate;         //댓글수정시간
//    private String photoOriginal;           // 원본 사진 경로
    private String photoLocal;              // 로컬 사진 경로
    private Long recipeNumber;              // 레시피 번호 (ID 대신 사용)
    private Long userNumber;
    private String userNickName;            // 작성자
    private String profilePic;
}
