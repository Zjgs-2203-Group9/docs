package com.word_learn.controller;

import com.word_learn.dto.LoginRequest;
import com.word_learn.dto.UserResponse;
import com.word_learn.entity.User;
import com.word_learn.service.UserService;
import com.word_learn.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name="用户接口")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Operation(summary = "注册接口")
    @PostMapping("/register")
    public Result<UserResponse> registerUser(@RequestBody User user) {
        UserResponse createdUser = userService.register(user);
        return Result.success(createdUser);
    }
    @Operation(summary = "登录接口")
    @PostMapping("/login")
    public Result<UserResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        UserResponse user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("用户名或密码错误！");
        }
    }
}
