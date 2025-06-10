package com.word_learn.dto;

import lombok.Data;

@Data

public class PageRequest {
    private Integer page;
    private Integer size;
    private Integer type;
    private String keyword;
}
