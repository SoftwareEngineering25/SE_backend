package com.dr.dto.manager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ManagerReportDTO {
    private int sirenNumber;
    private String userNickName;
    private String userEmail;
    private String sirenReason;
    private String sirenType;
    private String sirenDate;
    private int targetId;
}
