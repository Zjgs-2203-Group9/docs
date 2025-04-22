package com.word_learn.controller;

import com.word_learn.entity.Score;
import com.word_learn.entity.Word;
import com.word_learn.service.ScoreService;
import com.word_learn.service.WordService;
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

    @PostMapping("/collect")
    public ResponseEntity<Word> addWord(@RequestBody Word word) {
        Word savedWord = wordService.addWord(word);
        return new ResponseEntity<>(savedWord, HttpStatus.CREATED);
    }
    @PostMapping("/add")
    public ResponseEntity<Word> addNewWord(@RequestBody Word word) {
        Word createdWord = wordService.addWord(word);
        return new ResponseEntity<>(createdWord, HttpStatus.CREATED);
    }
    @GetMapping("/scores/{userId}")
    public ResponseEntity<List<Score>> getUserScores(@PathVariable Long userId) {
        List<Score> scores = scoreService.getScoresForUser(userId);
        return new ResponseEntity<>(scores, HttpStatus.OK);
    }

}
