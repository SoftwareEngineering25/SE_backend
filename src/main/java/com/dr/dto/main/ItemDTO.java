package com.dr.dto.main;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ItemDTO {
    @JsonProperty("기간2")
    private String month;
    @JsonProperty("지역1")
    private String country;
    @JsonProperty("가구평균배출량")
    private String amount;
}
