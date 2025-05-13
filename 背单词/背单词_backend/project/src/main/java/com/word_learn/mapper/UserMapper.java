package com.word_learn.mapper;

import com.word_learn.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findByUsername(String username);
    boolean existsByUsername(String username);

    User save(User user);
}
