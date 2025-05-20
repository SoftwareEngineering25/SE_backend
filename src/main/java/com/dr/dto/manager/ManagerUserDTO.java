package com.dr.dto.manager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ManagerUserDTO {
    private int userNumber;
    private String userNickname;
    private String userStatus;
    private String userPhone;
    private String userEmail;
}
