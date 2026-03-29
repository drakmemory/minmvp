package com.minmvp.service.impl;

import com.minmvp.mapper.UserMapper;
import com.minmvp.service.StatisticsService;
import com.minmvp.vo.StatisticsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger log = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Override
    public StatisticsVO getStatistics() {
        List<Map<String, Object>> dailyData = userMapper.selectDailyCount();
        List<Map<String, Object>> roleData = userMapper.selectRoleDistribution();

        List<StatisticsVO.DailyCount> daily = dailyData.stream()
                .map(m -> new StatisticsVO.DailyCount(
                        String.valueOf(m.get("date")),
                        ((Number) m.get("count")).intValue()))
                .collect(Collectors.toList());

        List<StatisticsVO.RoleCount> roles = roleData.stream()
                .map(m -> new StatisticsVO.RoleCount(
                        String.valueOf(m.get("name")),
                        ((Number) m.get("value")).intValue()))
                .collect(Collectors.toList());

        return new StatisticsVO(daily, roles);
    }
}
