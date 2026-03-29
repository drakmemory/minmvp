package com.minmvp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    private Long id;

    private String username;

    private String name;

    private String phone;

    private String email;

    private String role;

    private Integer status;

    private String avatar;

    private String createTime;
}
