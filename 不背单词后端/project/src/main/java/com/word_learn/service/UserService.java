package com.word_learn.service;

import com.word_learn.entity.User;

public interface UserService {
    User register(User user);
    User login(String username, String password);
}

