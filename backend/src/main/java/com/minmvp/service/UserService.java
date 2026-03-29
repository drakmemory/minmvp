package com.minmvp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.minmvp.common.PageResult;
import com.minmvp.dto.UserDTO;
import com.minmvp.entity.User;
import com.minmvp.vo.UserVO;

public interface UserService extends IService<User> {
    PageResult<UserVO> getUserPage(Integer page, Integer size, String keyword);
    UserVO getUserById(Long id);
    UserVO createUser(UserDTO dto);
    void updateUser(Long id, UserDTO dto);
    void deleteUser(Long id);
    User getByUsername(String username);
}
