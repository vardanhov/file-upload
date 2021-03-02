package com.example.uploadfile.config;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {

    /**
     * Маркеры для логера
     *
     * */
    @Bean
    public Marker adminMarker(){
        return MarkerFactory.getMarker("admin");
    }
    @Bean
    public Marker userMarker(){
        return MarkerFactory.getMarker("user");
    }
}
