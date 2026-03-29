package com.minmvp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.minmvp.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User selectByUsername(@Param("username") String username);

    @Select("SELECT role AS name, COUNT(*) AS value FROM sys_user WHERE deleted = 0 GROUP BY role")
    List<Map<String, Object>> selectRoleDistribution();

    @Select("SELECT DATE_FORMAT(create_time, '%m-%d') AS date, COUNT(*) AS count FROM sys_user WHERE deleted = 0 AND create_time >= DATE_SUB(NOW(), INTERVAL 12 DAY) GROUP BY DATE_FORMAT(create_time, '%m-%d') ORDER BY date")
    List<Map<String, Object>> selectDailyCount();
}
