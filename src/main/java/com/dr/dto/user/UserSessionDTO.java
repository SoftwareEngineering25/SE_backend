package com.dr.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
public class UserSessionDTO {
    private Long userNumber;
    private String userNickName;
    private String photoLocal;
}
