package com.word_learn.mapper;

import com.word_learn.dto.WordRequest;
import com.word_learn.entity.Word;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WordMapper {
    List<Word> findByUserId(Long userId);  // 根据用户 ID 获取该用户的单词列表
    boolean existsByWordAndUserId(@Param("word") String word, @Param("userId")Long userId);  // 判断用户是否已经收藏该单词

    void save(Word word);

    void saveToFavorite(Word word);

    void delete(Word word);

    void deleteFromFavorite(Word word);
    @Results({
            @Result(property = "word_id", column = "word_id"),
            @Result(property = "word_name", column = "word_name"),
            @Result(property = "word_meaning", column = "word_meaning"),
            @Result(property = "word_pronunciation", column = "word_pronunciation")
    })
    @Select("select w.id as word_id,w.word_name as word_name,w.word_pronunciation as word_pronunciation,w.word_meaning as word_meaning from words as w where w.type = #{type} limit 0,500")
    List<WordRequest> selectAllWords(Integer type);

}
