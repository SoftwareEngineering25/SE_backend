package com.dr.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FreeGoodDTO {
    private Long userNumber;
    private Long boardNumber;
    private Long goodNumber;
}
