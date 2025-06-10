package com.word_learn.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String email; // 假设在注册时需要邮件地址
}

