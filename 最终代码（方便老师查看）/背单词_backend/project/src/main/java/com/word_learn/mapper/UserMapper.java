package com.word_learn.mapper;

import com.word_learn.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username}")
    User findByUsername(String username);
    @Select("select exists(select 1 from user where username=#{username})")
    boolean existsByUsername(String username);
    void save(User user);
}
