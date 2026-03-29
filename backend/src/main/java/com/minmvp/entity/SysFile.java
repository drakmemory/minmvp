package com.minmvp.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("sys_file")
@NoArgsConstructor
@AllArgsConstructor
public class SysFile {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String originalName;

    private Long size;

    private String type;

    private String path;

    private String url;

    @TableField(fill = FieldFill.INSERT)
    private Date uploadTime;

    private Long uploaderId;

    private String uploaderName;

    @TableLogic
    private Integer deleted;
}
