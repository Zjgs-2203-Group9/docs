package com.word_learn.controller;

import com.word_learn.dto.WordRequest;
import com.word_learn.entity.Score;
import com.word_learn.entity.Word;
import com.word_learn.service.ScoreService;
import com.word_learn.service.WordService;
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
    @GetMapping("/collect/{userId}")
    public ResponseEntity<List<Word>> getCollectWords(@PathVariable Long userId) {
        List<Word> words = wordService.getCollectedWords(userId);
        return new ResponseEntity<>(words, HttpStatus.OK);
    }

    @GetMapping("/collect")
    public Result<List<WordRequest>> getAllWords(Integer type) {
        List<WordRequest> words = wordService.getWords(type);
        return Result.success(words);
    }
    @PostMapping("/add")
    public ResponseEntity addNewWord(@RequestBody Word word) {
        wordService.addWord(word);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
    @PostMapping("/delete")
    public ResponseEntity deleteWord(@RequestBody Word word) {
        wordService.deleteWord(word);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
    @GetMapping("/scores/{userId}")
    public ResponseEntity<List<Score>> getUserScores(@PathVariable Long userId) {
        List<Score> scores = scoreService.getScoresForUser(userId);
        return new ResponseEntity<>(scores, HttpStatus.OK);
    }

}
