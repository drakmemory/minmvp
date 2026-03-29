package com.minmvp.service;

import com.minmvp.vo.FileVO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    FileVO uploadFile(MultipartFile file, Long uploaderId, String uploaderName);
    Resource downloadFile(Long fileId);
    void deleteFile(Long fileId);
    List<FileVO> listFiles();
}
