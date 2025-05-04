package com.dr.dto.myPage;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PointCheckDTO {
    private Long userNumber;
    private int pointGet;
    private String pointNote;
    private String pointDate;
}
