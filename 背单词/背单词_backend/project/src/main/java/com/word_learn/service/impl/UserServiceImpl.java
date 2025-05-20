package com.word_learn.service.impl;

import ch.qos.logback.core.util.MD5Util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.word_learn.dto.UserResponse;
import com.word_learn.entity.User;
import com.word_learn.exceptionHandler.exceptions.UserException;
import com.word_learn.mapper.UserMapper;

import com.word_learn.service.UserService;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.security.Signature;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse register(User user) {
        // 在注册时可以检查用户名是否已存在
        if (userMapper.existsByUsername(user.getUsername())) {
            throw new UserException("用户已存在！");
        }
        // 加密密码
        user.setPassword(encryptPassword(user.getPassword()));
        userMapper.save(user);
        Calendar instance = Calendar.getInstance();
        String token = JWT.create().withClaim("userId", user.getId())
                .withClaim("userName", user.getUsername())
                .withExpiresAt(Instant.now().plusSeconds(3600))
                .sign(Algorithm.HMAC256("user"));
        UserResponse build = UserResponse.builder()
                .token(token)
                .username(user.getUsername())
                .build();
        return build;  // 登录成功
    }

    @Override
    public UserResponse login(String username, String password) {
        // 查找用户

        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(encryptPassword(password))) {
            String token = JWT.create().withClaim("userId", user.getId())
                    .withClaim("userName", user.getUsername())
                    .withExpiresAt(Instant.now().plusSeconds(3600))
                    .sign(Algorithm.HMAC256("user"));
            UserResponse build = UserResponse.builder()
                    .token(token)
                    .username(username)
                    .build();
            return build;  // 登录成功
        }
        return null;  // 登录失败
    }

    private String encryptPassword(String password) {
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        System.out.println(password);
        // 这里可以用更安全的加密方式
        return password;  // 简单的明文密码，实际开发中应使用哈希算法
    }
}

