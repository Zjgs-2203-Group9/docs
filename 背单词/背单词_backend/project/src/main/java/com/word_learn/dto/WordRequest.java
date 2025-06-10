package com.word_learn.dto;

import lombok.Data;

@Data
public class WordRequest {

    private Long word_id;
    private String word_name;
    private String word_meaning;
    private String word_pronunciation;//发音

}

