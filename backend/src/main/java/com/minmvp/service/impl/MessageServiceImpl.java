package com.minmvp.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.minmvp.common.PageResult;
import com.minmvp.config.KafkaConfig;
import com.minmvp.entity.Message;
import com.minmvp.mapper.MessageMapper;
import com.minmvp.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private MessageMapper messageMapper;

    @Autowired(required = false)
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public PageResult<Message> getMessages(Integer page, Integer size) {
        Page<Message> messagePage = new Page<>(page, size);
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("time");
        messageMapper.selectPage(messagePage, wrapper);
        return PageResult.from(messagePage);
    }

    @Override
    public void addMessage(String type, String userName, String content, String status) {
        Message message = new Message();
        message.setType(type);
        message.setUserName(userName);
        message.setContent(content);
        message.setStatus(status);
        message.setTime(new Date());
        messageMapper.insert(message);

        try {
            if (kafkaTemplate != null) {
                kafkaTemplate.send(KafkaConfig.TOPIC_OPERATION_LOG, JSON.toJSONString(message));
            }
        } catch (Exception e) {
            log.warn("Kafka send failed: {}", e.getMessage());
        }
    }
}
