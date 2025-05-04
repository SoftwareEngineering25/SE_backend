package com.dr.dto.manager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ManagerProductDTO {
    private String productName;
    private int productPrice;
    private String productCode;
    private int productCount;
}
