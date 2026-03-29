package com.minmvp.controller;

import com.minmvp.common.Result;
import com.minmvp.service.FileService;
import com.minmvp.service.MessageService;
import com.minmvp.vo.FileVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@Api(tags = "文件管理")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<FileVO> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        log.info("文件上传: {}", file.getOriginalFilename());
        Long uploaderId = (Long) request.getAttribute("userId");
        String uploaderName = (String) request.getAttribute("username");
        FileVO fileVO = fileService.uploadFile(file, uploaderId, uploaderName);
        messageService.addMessage("upload", uploaderName != null ? uploaderName : "系统", "上传文件：" + file.getOriginalFilename(), "success");
        return Result.success(fileVO);
    }

    @GetMapping("/{fileId}")
    @ApiOperation("文件下载")
    public ResponseEntity<Resource> download(@PathVariable Long fileId) {
        log.info("文件下载: fileId={}", fileId);
        Resource resource = fileService.downloadFile(fileId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment")
                .body(resource);
    }

    @GetMapping("/{fileId}/stream")
    @ApiOperation("文件流预览")
    public ResponseEntity<Resource> stream(@PathVariable Long fileId) {
        log.info("文件流预览: fileId={}", fileId);
        Resource resource = fileService.downloadFile(fileId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(resource);
    }

    @DeleteMapping("/{fileId}")
    @ApiOperation("删除文件")
    public Result<?> delete(@PathVariable Long fileId) {
        log.info("删除文件: fileId={}", fileId);
        fileService.deleteFile(fileId);
        return Result.success("删除成功");
    }

    @GetMapping
    @ApiOperation("文件列表")
    public Result<List<FileVO>> list() {
        log.info("查询文件列表");
        return Result.success(fileService.listFiles());
    }
}
