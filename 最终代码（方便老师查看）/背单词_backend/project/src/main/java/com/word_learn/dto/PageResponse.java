package com.word_learn.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponse {
    private Integer total;
    private List<WordRequest> words;
    private Integer pageNum;
    private Integer pageSize;
}
