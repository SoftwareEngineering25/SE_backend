package com.dr.dto.rank;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RankDTO {
    private int rank;              // 사용자 순위
    private String userNickname;   // 사용자 닉네임
    private String photoLocal;     // 사용자 프로필 사진
    private int scoreGet;          // 사용자 환경 기여 점수 합계
    private String profilePic;     // 카카오 로그인 사용자의 프로필 사진 (PROFILE_PIC 컬럼)
    private Long userNumber;
}


