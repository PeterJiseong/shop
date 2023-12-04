package com.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")
    String uploadpath;



    @Override
    //정적리소스(css,js,img)에 대한 요청을 처리하는 방법
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //브라우저에 입력한 url이 /imgages로 싲가하면 파일 ㅇ릭어옴
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadpath);
    }
}
