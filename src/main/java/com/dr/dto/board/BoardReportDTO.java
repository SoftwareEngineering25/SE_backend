package com.dr.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardReportDTO {
    private Long sirenNumber;
    private String sirenReason;
    private String sirenDate;
    private String sirenType;
    private Long boardNumber;
    private Long recipeNumber;
    private Long replyNumber;
    private Long userNumber;
}
