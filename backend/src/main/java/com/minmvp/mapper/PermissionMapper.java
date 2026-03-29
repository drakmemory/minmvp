package com.minmvp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.minmvp.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    @Select("SELECT * FROM sys_permission WHERE role = #{role}")
    List<Permission> selectByRole(@Param("role") String role);
}
