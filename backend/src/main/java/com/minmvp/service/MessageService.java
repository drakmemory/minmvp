package com.minmvp.service;

import com.minmvp.common.PageResult;
import com.minmvp.entity.Message;

public interface MessageService {
    PageResult<Message> getMessages(Integer page, Integer size);
    void addMessage(String type, String userName, String content, String status);
}
