package com.minmvp.dto;

import lombok.Data;

@Data
public class SearchDTO {

    private String keyword;

    private Integer page = 1;

    private Integer size = 10;
}
