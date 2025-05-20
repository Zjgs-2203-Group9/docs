package com.word_learn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
public class TestApplication {
    @Autowired
    ChatClient chatClient;
    @Autowired
    ObjectMapper objectMapper;
    JsonObject translate(String text){
        CloseableHttpClient build = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://api.siliconflow.cn/v1/chat/completions");
        httpPost.addHeader("Authorization","Bearer sk-zawiuqzwhrtkjnsyfncbnrsltdocnlixpdvpynnjsotjuodk");
        httpPost.addHeader("Content-Type","application/json");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("model","Qwen/Qwen3-32B");
        JsonObject messages2 = new JsonObject();
        messages2.addProperty("role","user");
        messages2.addProperty("content","It is hardly necessary for me to cite all the evidence of the depressing state of literacy. These figures from the Department of Education are sufficient: 27 million Americans cannot read at all. and a further 35million read al a level that is less than sufficient to survive in our society.");
        JsonObject messages3 = new JsonObject();
        messages3.addProperty("role","system");
        messages3.addProperty("content","You are a translator,You need to translate English to Chinese(If input language is English ) or  Chinese to English (If input language is Chinese ) "+"please respond in the format{text:....}");
        JsonArray array = new JsonArray();
        array.add(messages2);
        array.add(messages3);
        jsonObject.add("messages",array);
        JsonObject response_foramt = new JsonObject();
        response_foramt.addProperty("type","json_object");
        jsonObject.add("response_format",response_foramt);
        System.out.println(jsonObject.toString());
        httpPost.setEntity(new StringEntity(jsonObject.toString(), StandardCharsets.UTF_8));
        try {
            CloseableHttpResponse execute = build.execute(httpPost);
            build.close();
            execute.close();
            HttpEntity entity = execute.getEntity();
            try {
                String string = EntityUtils.toString(entity);
                JsonElement jsonObject1 =  JsonParser.parseString(string);
                return jsonObject1.getAsJsonObject().get("choices").getAsJsonArray().get(0).getAsJsonObject().get("message").getAsJsonObject().get("content").getAsJsonObject();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
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
}
