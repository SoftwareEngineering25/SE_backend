package com.dr.dto.myPage;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PointDetailDTO {
    private int pointNumber;
    private String pointNote;
    private int pointGet;
    private LocalDateTime pointDate;
    private int totalPoints;

    public String getFormattedPointDate() {
        if (pointDate != null) {
            Date date = Date.from(pointDate.atZone(ZoneId.systemDefault()).toInstant());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // 시간, 분, 초 제거
            return formatter.format(date);
        }
        return "";
    }

}
