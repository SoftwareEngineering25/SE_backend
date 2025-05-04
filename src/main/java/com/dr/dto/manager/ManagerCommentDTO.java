package com.dr.dto.manager;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ManagerCommentDTO {
    private int replyNumber;
    private String replyText;
    private String userNickName;
    private String userPhone;
    private String replyWriteDate;
    private int boardNumber;
    private int recipeNumber;
    private String boardType;
    private String recipeType;
}
