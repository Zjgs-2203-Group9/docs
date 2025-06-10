package com.word_learn.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.word_learn.dto.HttpTranslateEntity;
import com.word_learn.exceptionHandler.exceptions.UserException;
import com.word_learn.service.TranslateService;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class TranslateServiceImpl implements TranslateService {
    @Autowired
    ObjectMapper objectMapper;
    public String translateText(String text){
        CloseableHttpClient build = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://api.siliconflow.cn/v1/chat/completions");
        httpPost.addHeader("Authorization","Bearer sk-zawiuqzwhrtkjnsyfncbnrsltdocnlixpdvpynnjsotjuodk");
        httpPost.addHeader("Content-Type","application/json");
        HttpTranslateEntity instance = HttpTranslateEntity.getInstance();
        instance.send(text);
        String s="";
        try {
            s = objectMapper.writeValueAsString(instance);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if(s.isEmpty())throw new UserException("发生了错误！");
        httpPost.setEntity(new StringEntity(s, StandardCharsets.UTF_8));
        try {
            CloseableHttpResponse execute = build.execute(httpPost);
            HttpEntity entity = execute.getEntity();
            try {
                String string = EntityUtils.toString(entity);
                execute.close();
                build.close();
                JsonNode root = objectMapper.readTree(string);
                return root.at("/choices/0/message/content").asText();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
