package com.minmvp.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minmvp.common.PageResult;
import com.minmvp.config.KafkaConfig;
import com.minmvp.dto.UserDTO;
import com.minmvp.entity.User;
import com.minmvp.mapper.UserMapper;
import com.minmvp.service.UserService;
import com.minmvp.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String CACHE_KEY_PREFIX = "user:";
    private static final long CACHE_TTL_MINUTES = 30;

    @Autowired
    private UserMapper userMapper;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired(required = false)
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public PageResult<UserVO> getUserPage(Integer page, Integer size, String keyword) {
        Page<User> userPage = new Page<>(page, size);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like("name", keyword)
                    .or().like("phone", keyword)
                    .or().like("username", keyword));
        }
        wrapper.orderByDesc("create_time");
        userMapper.selectPage(userPage, wrapper);

        List<UserVO> voList = userPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        PageResult<UserVO> result = new PageResult<>();
        result.setTotal(userPage.getTotal());
        result.setList(voList);
        return result;
    }

    @Override
    public UserVO getUserById(Long id) {
        // Check Redis cache first
        String cacheKey = CACHE_KEY_PREFIX + id;
        try {
            if (redisTemplate != null) {
                Object cached = redisTemplate.opsForValue().get(cacheKey);
                if (cached != null) {
                    log.debug("Cache hit for user:{}", id);
                    return JSON.parseObject(JSON.toJSONString(cached), UserVO.class);
                }
            }
        } catch (Exception e) {
            log.warn("Redis cache read failed: {}", e.getMessage());
        }

        // Query database
        User user = userMapper.selectById(id);
        if (user == null) {
            return null;
        }
        UserVO vo = convertToVO(user);

        // Cache result
        try {
            if (redisTemplate != null) {
                redisTemplate.opsForValue().set(cacheKey, vo, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            log.warn("Redis cache write failed: {}", e.getMessage());
        }
        return vo;
    }

    @Override
    public UserVO createUser(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(SecureUtil.md5("123456"));
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole() != null ? dto.getRole() : "user");
        user.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        this.save(user);

        // Send Kafka message
        try {
            if (kafkaTemplate != null) {
                String message = JSON.toJSONString(user);
                kafkaTemplate.send(KafkaConfig.TOPIC_OPERATION_LOG, message);
            }
        } catch (Exception e) {
            log.warn("Kafka send failed: {}", e.getMessage());
        }

        return convertToVO(user);
    }

    @Override
    public void updateUser(Long id, UserDTO dto) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("User not found: " + id);
        }
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getRole() != null) user.setRole(dto.getRole());
        if (dto.getStatus() != null) user.setStatus(dto.getStatus());
        this.updateById(user);

        invalidateCache(id);
    }

    @Override
    public void deleteUser(Long id) {
        this.removeById(id);
        invalidateCache(id);
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
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
        if (user.getCreateTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            vo.setCreateTime(sdf.format(user.getCreateTime()));
        }
        return vo;
    }

    private void invalidateCache(Long id) {
        try {
            if (redisTemplate != null) {
                redisTemplate.delete(CACHE_KEY_PREFIX + id);
            }
        } catch (Exception e) {
            log.warn("Redis cache invalidation failed: {}", e.getMessage());
        }
    }
}
