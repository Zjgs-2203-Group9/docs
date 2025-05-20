package com.word_learn.service;

import com.word_learn.dto.UserResponse;
import com.word_learn.entity.User;

public interface UserService {
    UserResponse register(User user);
    UserResponse login(String username, String password);
}

