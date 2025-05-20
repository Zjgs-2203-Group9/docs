package com.word_learn.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.word_learn.utils.BaseContext;
import com.word_learn.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        try {
            String authorization = request.getHeader("Authorization");
            String[] split = authorization.split(" ");
            String token = split[1];
            log.info("jwt校验:{}", token);
            DecodedJWT decodedJWT = JwtUtil.getDecodedJWT(token);
            Long userId = Long.parseLong(decodedJWT.getClaim("userId").toString());
            BaseContext.setThreadUserId(userId);
            log.info("userId:" + userId);
            return true;
        }catch(Exception ex){
            System.out.println(ex.getMessage());
            response.setStatus(401);
            return false;
        }
    }
}
