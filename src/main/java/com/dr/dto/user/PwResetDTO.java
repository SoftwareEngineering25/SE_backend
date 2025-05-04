package com.dr.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor

//비밀번호 변경 DTO
public class PwResetDTO {
    private String userPhone;
    private String userPw;
}
