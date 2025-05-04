package com.dr.dto.myPage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserInfoDTO {
    private String userNickName;
    private String userEmail;
    private String userPhone;
    private int environmentScore;
    private int environmentRank;
    private int totalPoints;
    private String photo;
    private Long userNumber;
    private String profilePic; // 카카오 로그인 사용자의 프로필 사진
}
