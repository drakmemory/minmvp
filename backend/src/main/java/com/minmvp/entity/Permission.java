package com.minmvp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("sys_permission")
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String role;

    private String module;

    private Integer canView;

    private Integer canCreate;

    private Integer canEdit;

    private Integer canDelete;
}
