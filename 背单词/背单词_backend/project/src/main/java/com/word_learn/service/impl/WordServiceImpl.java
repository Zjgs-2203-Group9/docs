package com.word_learn.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.word_learn.dto.PageRequest;
import com.word_learn.dto.PageResponse;
import com.word_learn.dto.WordRequest;
import com.word_learn.entity.Favor;
import com.word_learn.entity.Word;
import com.word_learn.mapper.WordMapper;
import com.word_learn.service.WordService;
import com.word_learn.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
public class WordServiceImpl implements WordService {
    @Autowired
    private WordMapper wordMapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public List<WordRequest> getFavoriteWords(Long userId) {
        return wordMapper.findByUserId(userId);  // 获取用户收藏的单词
    }

    @Override
    @Transactional
    public void addWord(Word word) {
        // 可以添加逻辑检查用户是否已收藏该单词
        if (wordMapper.existsByWordAndUserId(word.getWord(), word.getUserId())) {
            throw new RuntimeException("Word already exists in your collection");
        }
        wordMapper.saveToFavorite(word);
    }

    @Override
    public void deleteWord(Word word) {
        //wordMapper.delete(word);
        wordMapper.deleteFromFavorite(word);
    }

    @Override

    public List<WordRequest> getWords(PageRequest pageRequest) {
        try {
//            PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize());
            //修改一下逻辑 现在先尝试去redis查找 再去mysql找避免大量查询的情况
            List<WordRequest> wordRequests;
            String s = stringRedisTemplate.opsForValue().get("words" + pageRequest.getType());
            if(s==null||s.isEmpty()){
                log.info("从数据库存入redis中");
                wordRequests = wordMapper.selectAllWords(pageRequest.getType());
                s = objectMapper.writeValueAsString(wordRequests);
                stringRedisTemplate.opsForValue().set("words"+pageRequest.getType(),s);
            }else{
                log.info("直接从redis获取");
                wordRequests = objectMapper.readValue(s, new TypeReference<List<WordRequest>>() {
                });
            }
//            PageInfo pageInfo = new PageInfo(wordRequests);
//            PageResponse build = PageResponse.builder()
//                    .pageNum(pageInfo.getPageNum())
//                    .words(wordRequests)
//                    .pageSize(pageInfo.getSize())
//                    .total(pageInfo.getSize())
//                    .build();
            return wordRequests;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally{
            PageHelper.clearPage();
        }
    }

    @Override
    public void addFavoriteWords(WordRequest word) {
        Favor favor = new Favor();
        favor.setWordId(word.getWord_id());
        favor.setUserId(BaseContext.getThreadUserId());
        wordMapper.insertFavoriteWord(favor);
    }

    @Override
    public void deleteFavoriteWord(WordRequest word) {
        Favor favor = new Favor();
        favor.setWordId(word.getWord_id());
        favor.setUserId(BaseContext.getThreadUserId());
        wordMapper.deleteFavoriteWord(favor);
    }

    @Override
    public Boolean getFavoriteWordStatus(Long id) {
        Favor favor = new Favor();
        favor.setWordId(id);
        favor.setUserId(BaseContext.getThreadUserId());
        return wordMapper.selectStatus(favor);
    }
}

