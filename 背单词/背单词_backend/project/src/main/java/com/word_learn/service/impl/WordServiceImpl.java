package com.word_learn.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WordServiceImpl implements WordService {
    @Autowired
    private WordMapper wordMapper;

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
            List<WordRequest> wordRequests = wordMapper.selectAllWords(pageRequest.getType());
//            PageInfo pageInfo = new PageInfo(wordRequests);
//            PageResponse build = PageResponse.builder()
//                    .pageNum(pageInfo.getPageNum())
//                    .words(wordRequests)
//                    .pageSize(pageInfo.getSize())
//                    .total(pageInfo.getSize())
//                    .build();
            return wordRequests;
        }finally{
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

