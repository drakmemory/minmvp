package com.minmvp.controller;

import com.minmvp.common.Result;
import com.minmvp.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/search")
@Api(tags = "全文搜索")
public class SearchController {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private SearchService searchService;

    @GetMapping("/users")
    @ApiOperation("搜索用户")
    public Result<Map<String, Object>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("搜索用户: keyword={}, page={}, size={}", keyword, page, size);
        return Result.success(searchService.searchUsers(keyword, page, size));
    }

    @PostMapping("/sync")
    @ApiOperation("同步数据到ES")
    public Result<?> sync() {
        log.info("同步数据到ES");
        searchService.syncDataToEs();
        return Result.success("同步完成");
    }
}
