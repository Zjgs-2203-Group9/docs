package com.word_learn.service;

import com.word_learn.dto.WordRequest;
import com.word_learn.entity.Word;

import java.util.List;

public interface WordService {
    List<Word> getCollectedWords(Long userId);
    void addWord(Word word);

    void deleteWord(Word word);

    List<WordRequest> getWords(Integer type);
}


