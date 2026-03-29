package com.minmvp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultVO {

    private Long id;

    private String name;

    private String phone;

    private String email;

    private String role;

    private Double score;
}
