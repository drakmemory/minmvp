package com.minmvp.controller;

import com.minmvp.common.Result;
import com.minmvp.entity.Task;
import com.minmvp.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Api(tags = "定时任务")
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @GetMapping
    @ApiOperation("查询任务列表")
    public Result<List<Task>> list() {
        log.info("查询任务列表");
        return Result.success(taskService.listTasks());
    }

    @PostMapping("/{taskId}/trigger")
    @ApiOperation("手动触发任务")
    public Result<?> trigger(@PathVariable Long taskId) {
        log.info("手动触发任务: taskId={}", taskId);
        taskService.triggerTask(taskId);
        return Result.success("任务已触发");
    }
}
