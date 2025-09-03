package com.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

 @Configuration
 public class WebConfig implements WebMvcConfigurer {
     @Bean
     public WebMvcConfigurer corsConfigurer() {
         return new WebMvcConfigurer() {
             @Override
             public void addCorsMappings(CorsRegistry registry) {
                 registry.addMapping("/**")
                     .allowedOrigins("http://localhost:8080", "http://localhost:7777") // Vue 서버 주소와 백엔드 서버 주소
                     .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                     .allowCredentials(true);
             }
         };
     }
 }