package com.dr.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HoneyBoardPhotoDTO {
    private Long photoNumber;
    private String photoOriginal;
    private String photoLocal;
    private Long photoSize;
    private String photoUpload;
    private Long userNumber;
    private Long boardNumber;

}
