package com.dr.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FreeBoardPhotoDTO {
    private Long photoNumber;
    private String photoOriginal;
    private String photoLocal;
    private Long photoSize;      // Long 타입으로 변경
    private String photoUpload; // 날짜/시간 관련이면 LocalDateTime 등이 더 적합할 수 있음
    private Long userNumber;
    private Long boardNumber;
}
