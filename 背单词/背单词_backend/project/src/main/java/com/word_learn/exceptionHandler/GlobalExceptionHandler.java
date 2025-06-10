package com.word_learn.exceptionHandler;

import com.word_learn.exceptionHandler.exceptions.BaseException;
import com.word_learn.utils.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result handler(SQLIntegrityConstraintViolationException ex){
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String username = split[2];
            String msg = username + "已存在";
            return Result.error(msg);
        }else{
            return Result.error("未知错误");
        }
    }
    @ExceptionHandler
    public Result handler(BaseException ex){
        return Result.error(ex.getMessage());
    }
}
