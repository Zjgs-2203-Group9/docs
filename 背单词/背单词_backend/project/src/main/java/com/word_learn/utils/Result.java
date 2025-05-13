package com.word_learn.utils;

import lombok.Data;

@Data
public class Result<T>{
    private int code;
    private String msg;//错误消息
    private Object extra;
    public static <T> Result<T> success(){
        Result<T> tResult = new Result<>();
        tResult.code=1;
        return tResult;
    }
    public static <T> Result<T> success(T data){
        Result<T> tResult = new Result<>();
        tResult.extra = data;
        tResult.code=1;
        return tResult;
    }
    public static <T> Result<T> error(String msg){
        Result<T> tResult = new Result<>();
        tResult.msg = msg;
        tResult.code=0;
        return tResult;
    }
}
