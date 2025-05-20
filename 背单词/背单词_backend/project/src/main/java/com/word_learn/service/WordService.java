package com.word_learn.service;

import com.word_learn.dto.PageRequest;
import com.word_learn.dto.PageResponse;
import com.word_learn.dto.WordRequest;
import com.word_learn.entity.Word;

import java.util.List;

public interface WordService {
    List<WordRequest> getFavoriteWords(Long userId);
    void addWord(Word word);

    void deleteWord(Word word);

    List<WordRequest> getWords(PageRequest pageRequest);

    void addFavoriteWords(WordRequest word);

    void deleteFavoriteWord(WordRequest word);

    Boolean getFavoriteWordStatus(Long id);
}


