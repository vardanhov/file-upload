package com.example.uploadfile.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/templates/css/vuetify.min.css")
                .addResourceLocations("classpath:/templates/css/materialdesignicons.min.css");

        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/templates/fonts/materialdesignicons-webfont.woff2");

        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/templates/favicon.ico");

    }
}
