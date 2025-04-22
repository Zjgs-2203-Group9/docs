package com.word_learn.service;

import com.word_learn.entity.Word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.List;

public interface WordService {
    List<Word> getCollectedWords(Long userId);
    Word addWord(Word word);
}


