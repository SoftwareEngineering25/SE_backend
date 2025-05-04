package com.dr.dto.myPage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserPostDTO {
    private int myPostSeq;
    private String myPostTitle;
    private String myPostType;
    private int myPostgoodCount;
    private String myPostWriteDate;
    private int userNumber;
    private int boardNumber;
}
