package com.minmvp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.minmvp.entity.SysFile;
import com.minmvp.mapper.SysFileMapper;
import com.minmvp.service.FileService;
import com.minmvp.vo.FileVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${file.upload-path}")
    private String uploadPath;

    @Autowired
    private SysFileMapper sysFileMapper;

    @Override
    public FileVO uploadFile(MultipartFile file, Long uploaderId, String uploaderName) {
        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        String storedName = UUID.randomUUID().toString().replace("-", "") + extension;

        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dest = new File(dir, storedName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.error("File upload failed: {}", e.getMessage());
            throw new RuntimeException("File upload failed", e);
        }

        SysFile sysFile = new SysFile();
        sysFile.setName(storedName);
        sysFile.setOriginalName(originalName);
        sysFile.setSize(file.getSize());
        sysFile.setType(file.getContentType());
        sysFile.setPath(dest.getAbsolutePath());
        sysFile.setUrl("/api/file/download/" + storedName);
        sysFile.setUploaderId(uploaderId);
        sysFile.setUploaderName(uploaderName);
        sysFileMapper.insert(sysFile);

        return convertToVO(sysFile);
    }

    @Override
    public Resource downloadFile(Long fileId) {
        SysFile sysFile = sysFileMapper.selectById(fileId);
        if (sysFile == null) {
            throw new RuntimeException("File not found: " + fileId);
        }
        File file = new File(sysFile.getPath());
        if (!file.exists()) {
            throw new RuntimeException("File not found on disk: " + sysFile.getPath());
        }
        return new FileSystemResource(file);
    }

    @Override
    public void deleteFile(Long fileId) {
        sysFileMapper.deleteById(fileId);
    }

    @Override
    public List<FileVO> listFiles() {
        QueryWrapper<SysFile> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("upload_time");
        List<SysFile> files = sysFileMapper.selectList(wrapper);
        return files.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private FileVO convertToVO(SysFile sysFile) {
        FileVO vo = new FileVO();
        vo.setFileId(String.valueOf(sysFile.getId()));
        vo.setUrl(sysFile.getUrl());
        vo.setName(sysFile.getOriginalName());
        vo.setSize(formatFileSize(sysFile.getSize()));
        vo.setType(sysFile.getType());
        if (sysFile.getUploadTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            vo.setUploadTime(sdf.format(sysFile.getUploadTime()));
        }
        vo.setUploader(sysFile.getUploaderName());
        return vo;
    }

    private String formatFileSize(Long size) {
        if (size == null) return "0 B";
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.1f MB", size / (1024.0 * 1024));
        return String.format("%.1f GB", size / (1024.0 * 1024 * 1024));
    }
}
