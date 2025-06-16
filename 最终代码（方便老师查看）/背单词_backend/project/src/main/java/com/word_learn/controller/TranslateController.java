package com.word_learn.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.word_learn.dto.TranslateResponse;
import com.word_learn.service.TranslateService;
import com.word_learn.utils.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Tag(name="翻译接口")
@RestController
@RequestMapping("/api/translate")
public class TranslateController {
    @Autowired
    TranslateService translateService;
    @Autowired
    ObjectMapper objectMapper;
    @Tag(name="AI翻译接口")
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
