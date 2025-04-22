package com.word_learn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;  // 单词
    private String meaning;  // 单词的含义

    private Long userId;  // 与用户的关联，表示哪个用户收藏了这个单词

    // 省略了其他可能需要的字段，如创建时间、更新时间等

}


