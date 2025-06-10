package com.word_learn.utils;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal();

    public static void setThreadUserId(Long userId){
        threadLocal.set(userId);
    }
    public static Long getThreadUserId(){
        return threadLocal.get();
    }


}
