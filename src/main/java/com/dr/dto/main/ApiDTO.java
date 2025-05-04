package com.dr.dto.main;

import lombok.Data;

import java.util.List;

@Data
public class ApiDTO {
    private int currentCount;
    private int page;
    private List<ItemDTO> data;
}