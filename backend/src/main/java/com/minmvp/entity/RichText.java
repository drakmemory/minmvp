package com.minmvp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("sys_rich_text")
@NoArgsConstructor
@AllArgsConstructor
public class RichText {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    private Long creatorId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableLogic
    private Integer deleted;
}
