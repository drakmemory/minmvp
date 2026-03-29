package com.minmvp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("sys_message")
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String type;

    private String userName;

    private String content;

    private Date time;

    private Integer status;

    @TableLogic
    private Integer deleted;
}
