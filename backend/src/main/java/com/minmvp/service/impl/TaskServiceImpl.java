package com.minmvp.service.impl;

import com.minmvp.entity.Task;
import com.minmvp.mapper.TaskMapper;
import com.minmvp.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<Task> listTasks() {
        return taskMapper.selectList(null);
    }

    @Override
    public void triggerTask(Long taskId) {
        Task task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found: " + taskId);
        }

        task.setStatus("running");
        taskMapper.updateById(task);

        // Simulate task execution
        Random random = new Random();
        int duration = random.nextInt(5000) + 500;
        task.setStatus("success");
        task.setLastRun(new Date());
        task.setDuration(duration + "ms");
        taskMapper.updateById(task);

        log.info("Task [{}] executed successfully in {}ms", task.getName(), duration);
    }
}
