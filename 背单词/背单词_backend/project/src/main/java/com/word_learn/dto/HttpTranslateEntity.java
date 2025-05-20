package com.word_learn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
public class HttpTranslateEntity {
    private static volatile HttpTranslateEntity httpTranslateEntity;
    private String model="Qwen/Qwen3-32B";
    private List<Message> messages = new ArrayList<>();
    @JsonProperty("response_format")
    private ResponseFormat responseFormat=new ResponseFormat("json_object");
    private  void addMessages(String role,String content){
        messages.add(new Message(role,content));
    }
    public void send(String message){
        messages.get(1).content = message;
    }
    @Data
    @AllArgsConstructor
    static class Message{
        String role;
        String content;
    }
    @Data
    @AllArgsConstructor
    static class ResponseFormat{
        String type;
    }
    public static HttpTranslateEntity getInstance() {
        if(httpTranslateEntity==null){
            synchronized (HttpTranslateEntity.class){
                if(httpTranslateEntity==null){
                    httpTranslateEntity = new HttpTranslateEntity();
                    httpTranslateEntity.addMessages("system","You are a translator,You need to translate English to Chinese(If input language is English ) or  Chinese to English (If input language is Chinese ) "+"please respond in the format{text:....}");
                    httpTranslateEntity.addMessages("user","你好！");
                }
            }
        }
        return httpTranslateEntity;
    }
}
