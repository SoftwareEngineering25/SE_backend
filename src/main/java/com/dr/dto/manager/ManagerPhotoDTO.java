package com.dr.dto.manager;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ManagerPhotoDTO {
    private int photoNumber;
    private String photoOriginal;
    private String photoLocal;
    private String photoSize;
    private String photoUpload;
    private int productName;
    private int productNumber;
}
