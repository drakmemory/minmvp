package com.minmvp.controller;

import com.minmvp.common.Result;
import com.minmvp.service.ExcelService;
import com.minmvp.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
@Api(tags = "Excel导入导出")
public class ExcelController {

    private static final Logger log = LoggerFactory.getLogger(ExcelController.class);

    @Autowired
    private ExcelService excelService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/export/users")
    @ApiOperation("导出用户Excel")
    public void exportUsers(HttpServletResponse response, HttpServletRequest request) {
        log.info("导出用户Excel");
        String operator = (String) request.getAttribute("username");
        excelService.exportUsers(response);
        messageService.addMessage("export", operator != null ? operator : "系统", "导出用户数据Excel", "success");
    }

    @PostMapping("/import/users")
    @ApiOperation("导入用户Excel")
    public Result<Map<String, Object>> importUsers(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        log.info("导入用户Excel");
        String operator = (String) request.getAttribute("username");
        Map<String, Object> result = excelService.importUsers(file);
        messageService.addMessage("import", operator != null ? operator : "系统", "导入用户数据Excel", "success");
        return Result.success(result);
    }
}
