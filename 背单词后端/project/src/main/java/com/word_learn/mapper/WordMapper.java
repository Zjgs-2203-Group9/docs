package com.word_learn.mapper;

import com.word_learn.entity.Word;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WordMapper {
    List<Word> findByUserId(Long userId);  // 根据用户 ID 获取该用户的单词列表
    boolean existsByWordAndUserId(String word, Long userId);  // 判断用户是否已经收藏该单词

    Word save(Word word);
}
