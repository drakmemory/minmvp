package com.minmvp.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface ExcelService {
    void exportUsers(HttpServletResponse response);
    Map<String, Object> importUsers(MultipartFile file);
}
