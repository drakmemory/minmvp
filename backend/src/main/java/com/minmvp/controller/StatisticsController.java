package com.minmvp.controller;

import com.minmvp.common.Result;
import com.minmvp.service.StatisticsService;
import com.minmvp.vo.StatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
@Api(tags = "数据统计")
public class StatisticsController {

    private static final Logger log = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    @ApiOperation("获取统计数据")
    public Result<StatisticsVO> getStatistics() {
        log.info("获取统计数据");
        return Result.success(statisticsService.getStatistics());
    }
}
