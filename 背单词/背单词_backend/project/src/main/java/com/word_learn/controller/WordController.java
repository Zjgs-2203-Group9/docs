package com.word_learn.controller;

import com.word_learn.dto.PageRequest;
import com.word_learn.dto.PageResponse;
import com.word_learn.dto.WordRequest;
import com.word_learn.entity.Score;
import com.word_learn.entity.Word;
import com.word_learn.service.ScoreService;
import com.word_learn.service.WordService;
import com.word_learn.utils.BaseContext;
import com.word_learn.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "单词类接口(带收藏)")
@RestController
@RequestMapping("/api/words")
public class WordController {

    @Autowired
    private WordService wordService;
    @Autowired
    private ScoreService scoreService;

    @Operation(summary = "收藏表获取接口")
    @GetMapping("/favor")
    public Result<List<WordRequest>> getFavoriteWords() {
        Long userId = BaseContext.getThreadUserId();
        List<WordRequest> words = wordService.getFavoriteWords(userId);
        return Result.success(words);
    }

    @Operation(summary = "收藏表添加接口")
    @PostMapping("/favor")
    public Result<String> addFavoriteWords(@RequestBody WordRequest word) {
        wordService.addFavoriteWords(word);
        return Result.success();
    }

    @Operation(summary = "收藏表状态获取接口")
    @GetMapping("/favor/status")
    public Result<Boolean> getStatus(@RequestParam("word_id") Long id) {
        Boolean is = wordService.getFavoriteWordStatus(id);
        return Result.success(is);
    }

    @Operation(summary = "收藏表删除接口")
    @DeleteMapping("/favor")
    public Result<String> deleteFavoriteWord(@RequestBody WordRequest word) {
        wordService.deleteFavoriteWord(word);
        return Result.success();
    }

    @Operation(summary = "获取单词接口")
    @GetMapping("/collect")
    public Result<List<WordRequest>> getAllWords(PageRequest pageRequest) {
        List<WordRequest> words = wordService.getWords(pageRequest);
        return Result.success(words);
    }

//    @PostMapping("/add")
//    public ResponseEntity addNewWord(@RequestBody Word word) {
//        wordService.addWord(word);
//        return new ResponseEntity<>(null,HttpStatus.OK);
//    }
    @Operation(summary = "获取分数接口")
    @GetMapping("/scores/{userId}")
    public Result<List<Score>> getUserScores(@PathVariable Long userId) {
        List<Score> scores = scoreService.getScoresForUser(userId);
        return Result.success(scores);

    }

}
