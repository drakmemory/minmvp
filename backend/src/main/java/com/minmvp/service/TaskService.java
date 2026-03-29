package com.minmvp.service;

import com.minmvp.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> listTasks();
    void triggerTask(Long taskId);
}
