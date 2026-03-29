package com.minmvp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("sys_task")
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String cron;

    private String handler;

    private Date lastRun;

    private Date nextRun;

    private String status;

    private String duration;

    @TableLogic
    private Integer deleted;
}
