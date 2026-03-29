package com.minmvp.controller;

import com.minmvp.common.PageResult;
import com.minmvp.common.Result;
import com.minmvp.dto.UserDTO;
import com.minmvp.service.MessageService;
import com.minmvp.service.UserService;
import com.minmvp.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
@Api(tags = "用户管理")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @GetMapping
    @ApiOperation("分页查询用户列表")
    public Result<PageResult<UserVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        log.info("分页查询用户列表: page={}, size={}, keyword={}", page, size, keyword);
        return Result.success(userService.getUserPage(page, size, keyword));
    }

    @GetMapping("/{id}")
    @ApiOperation("查询用户详情")
    public Result<UserVO> getById(@PathVariable Long id) {
        log.info("查询用户详情: id={}", id);
        UserVO user = userService.getUserById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }

    @PostMapping
    @ApiOperation("新增用户")
    public Result<UserVO> create(@RequestBody UserDTO dto, HttpServletRequest request) {
        log.info("新增用户: {}", dto.getName());
        UserVO created = userService.createUser(dto);
        String operator = (String) request.getAttribute("username");
        messageService.addMessage("create", operator != null ? operator : "系统", "新增用户：" + dto.getName(), "success");
        return Result.success(created);
    }

    @PutMapping("/{id}")
    @ApiOperation("更新用户")
    public Result<?> update(@PathVariable Long id, @RequestBody UserDTO dto, HttpServletRequest request) {
        log.info("更新用户: id={}", id);
        userService.updateUser(id, dto);
        String operator = (String) request.getAttribute("username");
        messageService.addMessage("update", operator != null ? operator : "系统", "更新用户：" + dto.getName(), "success");
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除用户")
    public Result<?> delete(@PathVariable Long id, HttpServletRequest request) {
        log.info("删除用户: id={}", id);
        userService.deleteUser(id);
        String operator = (String) request.getAttribute("username");
        messageService.addMessage("delete", operator != null ? operator : "系统", "删除用户ID：" + id, "warning");
        return Result.success("删除成功");
    }
}
