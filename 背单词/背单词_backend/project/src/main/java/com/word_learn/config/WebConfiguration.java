package com.word_learn.config;

import com.word_learn.interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 使用allowedOriginPatterns替换allowedOrigins
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true) // 保持凭据开启
                .maxAge(3600);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor( new UserInterceptor()).addPathPatterns("/api/**").excludePathPatterns("/api/users/*");
    }
}
