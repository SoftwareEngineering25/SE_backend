package com.dr.dto.manager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ManagerPointDTO {
    private int pointNumber;
    private String userNickName;
    private String userEmail;
    private String pointNote;
    private int pointGet;
    private String pointDate;
    private int PointSum;
}
