package com.minmvp.controller;

import com.minmvp.common.Result;
import com.minmvp.entity.Permission;
import com.minmvp.mapper.PermissionMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@Api(tags = "权限控制")
public class PermissionController {

    private static final Logger log = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    private PermissionMapper permissionMapper;

    @GetMapping
    @ApiOperation("查询角色权限")
    public Result<List<Permission>> getPermissions(@RequestParam(required = false) String role) {
        log.info("查询角色权限: role={}", role);
        if (role != null && !role.isEmpty()) {
            return Result.success(permissionMapper.selectByRole(role));
        }
        return Result.success(permissionMapper.selectList(null));
    }
}
