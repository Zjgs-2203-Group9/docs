package com.word_learn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.word_learn.dto.HttpTranslateEntity;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.BasicHttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
public class TestApplication {
    @Autowired
    ObjectMapper objectMapper;
    @Test
    void test2(){
        HttpTranslateEntity instance = HttpTranslateEntity.getInstance();
        instance.send("你好啊");
        try {
            String s = objectMapper.writeValueAsString(instance);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Test
    void test3(){

        stringRedisTemplate.opsForValue().set("kinoasdasd","123");
    }
}
