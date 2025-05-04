package com.dr.dto.myPage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class CheckDTO {
    private Long dailyNumber;   // 출석 기록 번호
    private String dailyDate;   // 출석 날짜 (문자열로 변경)
    private Long userNumber;     // 유저 번호
}
