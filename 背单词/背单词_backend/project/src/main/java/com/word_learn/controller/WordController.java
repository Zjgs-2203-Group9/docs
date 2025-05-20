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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/words")
public class WordController {

    @Autowired
    private WordService wordService;
    @Autowired
    private ScoreService scoreService;
    @GetMapping("/favor")
    public Result<List<WordRequest>> getFavoriteWords() {
        Long userId = BaseContext.getThreadUserId();
        List<WordRequest> words = wordService.getFavoriteWords(userId);
        return Result.success(words);
    }
    @PostMapping("/favor")
    public Result<String> addFavoriteWords(@RequestBody WordRequest word) {
        wordService.addFavoriteWords(word);
        return Result.success();
    }
    @GetMapping("/favor/status")
    public Result<Boolean> getStatus(@RequestParam("word_id") Long id) {
        Boolean is = wordService.getFavoriteWordStatus(id);
        return Result.success(is);
    }
    @DeleteMapping("/favor")
    public Result<String> deleteFavoriteWord(@RequestBody WordRequest word) {
        wordService.deleteFavoriteWord(word);
        return Result.success();
    }
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
    @GetMapping("/scores/{userId}")
    public ResponseEntity<List<Score>> getUserScores(@PathVariable Long userId) {
        List<Score> scores = scoreService.getScoresForUser(userId);
        return new ResponseEntity<>(scores, HttpStatus.OK);
    }

}
