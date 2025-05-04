package com.dr.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
public class UserDTO {
    private Long userNumber;
    private String userEmail;
    private String userPw;
    private String userNickName;
    private String userPhone;
    private String provider;
    private String providerId;
    private String profilePic;
    private Long pointNumber;
    private Long scoreNumber;
    private Long photoNumber;
}
