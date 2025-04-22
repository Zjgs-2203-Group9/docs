package com.word_learn.service.impl;

import com.word_learn.entity.Word;
import com.word_learn.mapper.WordMapper;
import com.word_learn.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private WordMapper wordMapper;

    @Override
    public List<Word> getCollectedWords(Long userId) {
        return wordMapper.findByUserId(userId);  // 获取用户收藏的单词
    }

    @Override
    public Word addWord(Word word) {
        // 可以添加逻辑检查用户是否已收藏该单词
        if (wordMapper.existsByWordAndUserId(word.getWord(), word.getUserId())) {
            throw new RuntimeException("Word already exists in your collection");
        }
        return wordMapper.save(word);  // 保存单词
    }
}

