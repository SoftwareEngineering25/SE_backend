package com.dr.dto.myPage;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PointRecordDTO {
    private int userNumber;
    private int points;
    private String note;

    public PointRecordDTO(Long userNumber, int i, String 개근) {
    }
}
