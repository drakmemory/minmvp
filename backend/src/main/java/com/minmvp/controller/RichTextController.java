package com.minmvp.controller;

import com.minmvp.common.Result;
import com.minmvp.dto.RichTextDTO;
import com.minmvp.entity.RichText;
import com.minmvp.service.RichTextService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/richtext")
@Api(tags = "富文本管理")
public class RichTextController {

    private static final Logger log = LoggerFactory.getLogger(RichTextController.class);

    @Autowired
    private RichTextService richTextService;

    @PostMapping("/save")
    @ApiOperation("保存富文本")
    public Result<RichText> save(@RequestBody RichTextDTO dto, HttpServletRequest request) {
        log.info("保存富文本: {}", dto.getTitle());
        Long creatorId = (Long) request.getAttribute("userId");
        return Result.success(richTextService.save(dto, creatorId));
    }

    @GetMapping("/{id}")
    @ApiOperation("获取富文本")
    public Result<RichText> getById(@PathVariable Long id) {
        log.info("获取富文本: id={}", id);
        RichText richText = richTextService.getById(id);
        if (richText == null) {
            return Result.error("文档不存在");
        }
        return Result.success(richText);
    }

    @GetMapping
    @ApiOperation("富文本列表")
    public Result<List<RichText>> list() {
        log.info("查询富文本列表");
        return Result.success(richTextService.list());
    }
}
