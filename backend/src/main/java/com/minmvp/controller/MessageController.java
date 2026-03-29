package com.minmvp.controller;

import com.minmvp.common.PageResult;
import com.minmvp.common.Result;
import com.minmvp.entity.Message;
import com.minmvp.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@Api(tags = "消息通知")
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @GetMapping
    @ApiOperation("获取消息列表")
    public Result<PageResult<Message>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("获取消息列表: page={}, size={}", page, size);
        return Result.success(messageService.getMessages(page, size));
    }
}
