package com.dr.dto.manager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ManagerRegisterDTO {
        private int productNumber;
        private String productName;
        private String productCode;
        private int productPrice;
        private String photoLocal;
}
