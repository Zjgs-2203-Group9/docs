package com.word_learn.service.impl;

import com.word_learn.entity.Score;
import com.word_learn.mapper.ScoreMapper;
import com.word_learn.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Override
    public List<Score> getScoresForUser(Long userId) {
        return scoreMapper.findByUserId(userId);  // 获取用户的所有成绩
    }

    @Override
    public Score updateScore(Score score) {
        // 可以添加一些业务逻辑检查（如用户是否存在、成绩是否有效等）
        return scoreMapper.save(score);  // 更新成绩
    }
}

