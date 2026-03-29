package com.minmvp.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.minmvp.entity.User;
import com.minmvp.mapper.UserMapper;
import com.minmvp.service.ExcelService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExcelServiceImpl implements ExcelService {

    private static final Logger log = LoggerFactory.getLogger(ExcelServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Data
    public static class UserExcelData {
        @ExcelProperty("Username")
        private String username;

        @ExcelProperty("Name")
        private String name;

        @ExcelProperty("Phone")
        private String phone;

        @ExcelProperty("Email")
        private String email;

        @ExcelProperty("Role")
        private String role;
    }

    @Override
    public void exportUsers(HttpServletResponse response) {
        try {
            List<User> users = userMapper.selectList(null);
            List<UserExcelData> dataList = users.stream().map(user -> {
                UserExcelData data = new UserExcelData();
                data.setUsername(user.getUsername());
                data.setName(user.getName());
                data.setPhone(user.getPhone());
                data.setEmail(user.getEmail());
                data.setRole(user.getRole());
                return data;
            }).collect(Collectors.toList());

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("users", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

            EasyExcel.write(response.getOutputStream(), UserExcelData.class)
                    .sheet("Users")
                    .doWrite(dataList);
        } catch (IOException e) {
            log.error("Excel export failed: {}", e.getMessage());
            throw new RuntimeException("Excel export failed", e);
        }
    }

    @Override
    public Map<String, Object> importUsers(MultipartFile file) {
        List<UserExcelData> successList = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        try {
            EasyExcel.read(file.getInputStream(), UserExcelData.class, new AnalysisEventListener<UserExcelData>() {
                @Override
                public void invoke(UserExcelData data, AnalysisContext context) {
                    if (data.getUsername() == null || data.getUsername().trim().isEmpty()) {
                        errorMessages.add("Row " + context.readRowHolder().getRowIndex() + ": username is empty");
                        return;
                    }
                    if (data.getName() == null || data.getName().trim().isEmpty()) {
                        errorMessages.add("Row " + context.readRowHolder().getRowIndex() + ": name is empty");
                        return;
                    }
                    successList.add(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    log.info("Excel parsing completed, {} valid rows", successList.size());
                }
            }).sheet().doRead();
        } catch (IOException e) {
            log.error("Excel import read failed: {}", e.getMessage());
            throw new RuntimeException("Excel import failed", e);
        }

        int insertedCount = 0;
        for (UserExcelData data : successList) {
            try {
                User user = new User();
                user.setUsername(data.getUsername());
                user.setPassword(SecureUtil.md5("123456"));
                user.setName(data.getName());
                user.setPhone(data.getPhone());
                user.setEmail(data.getEmail());
                user.setRole(data.getRole() != null ? data.getRole() : "user");
                user.setStatus(1);
                userMapper.insert(user);
                insertedCount++;
            } catch (Exception e) {
                errorMessages.add("Failed to insert user [" + data.getUsername() + "]: " + e.getMessage());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", insertedCount);
        result.put("failed", errorMessages.size());
        result.put("errors", errorMessages);
        return result;
    }
}
