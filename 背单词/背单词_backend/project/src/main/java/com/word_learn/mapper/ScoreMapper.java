package com.word_learn.mapper;

import com.word_learn.entity.Score;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScoreMapper {
    List<Score> findByUserId(Long userId);  // 根据用户 ID 获取该用户的所有成绩

    Score save(Score score);
}
