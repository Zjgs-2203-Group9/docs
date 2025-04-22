package com.word_learn.service;

import com.word_learn.entity.Score;

import java.util.List;

public interface ScoreService {
    List<Score> getScoresForUser(Long userId);
    Score updateScore(Score score);
}

