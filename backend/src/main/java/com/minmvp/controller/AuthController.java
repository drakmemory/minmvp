package com.minmvp.controller;

import cn.hutool.crypto.SecureUtil;
import com.minmvp.common.Result;
import com.minmvp.dto.LoginDTO;
import com.minmvp.entity.User;
import com.minmvp.service.MessageService;
import com.minmvp.service.UserService;
import com.minmvp.util.JwtUtil;
import com.minmvp.vo.LoginVO;
import com.minmvp.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api")
@Api(tags = "认证管理")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MessageService messageService;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<LoginVO> login(@RequestBody LoginDTO dto) {
        log.info("用户登录请求: {}", dto.getUsername());
        User user = userService.getByUsername(dto.getUsername());
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!SecureUtil.md5(dto.getPassword()).equals(user.getPassword())) {
            return Result.error("密码错误");
        }
        if (user.getStatus() == 0) {
            return Result.error("账号已禁用");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        UserVO userVO = convertToVO(user);
        loginVO.setUser(userVO);
        messageService.addMessage("login", user.getName(), "用户登录系统", "success");
        return Result.success(loginVO);
    }

    @PostMapping("/logout")
    @ApiOperation("用户登出")
    public Result<?> logout() {
        log.info("用户登出");
        return Result.success("登出成功");
    }

    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setName(user.getName());
        vo.setPhone(user.getPhone());
        vo.setEmail(user.getEmail());
        vo.setRole(user.getRole());
        vo.setStatus(user.getStatus());
        vo.setAvatar(user.getAvatar());
        vo.setCreateTime(user.getCreateTime() != null
                ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(user.getCreateTime()) : null);
        return vo;
    }
}
