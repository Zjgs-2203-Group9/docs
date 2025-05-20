package com.word_learn.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.word_learn.dto.TranslateResponse;
import com.word_learn.service.TranslateService;
import com.word_learn.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/translate")
public class TranslateController {
    @Autowired
    TranslateService translateService;
    @Autowired
    ObjectMapper objectMapper;
    @PostMapping("")
    public Result<TranslateResponse> translate(@RequestBody String text){
        String json = translateService.translateText(text);
        try {
            TranslateResponse translateResponse = objectMapper.readValue(json, TranslateResponse.class);
            return Result.success(translateResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
