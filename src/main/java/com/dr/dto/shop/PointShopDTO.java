package com.dr.dto.shop;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PointShopDTO {
    //포인트샵 페이지 필드
    private Long userNumber;
    private Long totalPoint;
    private String photoLocal;
    private String productName;
    private Long productPrice;
    private Long productCount;
    private Long quantity;
    private String userPhone;
    private Long totalCost;
}
