package com.word_learn.service.impl;

import com.word_learn.entity.User;
import com.word_learn.mapper.UserMapper;

import com.word_learn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User register(User user) {
        // 在注册时可以检查用户名是否已存在
        if (userMapper.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        // 加密密码
        user.setPassword(encryptPassword(user.getPassword()));
        return userMapper.save(user);
    }

    @Override
    public User login(String username, String password) {
        // 查找用户
        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(encryptPassword(password))) {
            return user;  // 登录成功
        }
        return null;  // 登录失败
    }

    private String encryptPassword(String password) {
        // 这里可以用更安全的加密方式
        return password;  // 简单的明文密码，实际开发中应使用哈希算法
    }
}

