package com.example.springsecurityapplication.config;


//КОНФИГУРАЦИЯ ДЛЯ ЗАГРУЗКИ ФОТО

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//данный файл будет конфигурацией
@Configuration
public class Config implements WebMvcConfigurer {

    //внедряем данные в поле класса(конфигурации), т. е. путь, где хранятся фото
    @Value("${upload.path}")
    private String uploadPath;
                   //переменная, в которой лежит путь к медиафайлам(прописан в в appllication.properties)

    //!переопределили метод в ResourceHandlers!
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //!изменяем строку метода на:
        registry.addResourceHandler("/img/**") //если обращаемся в шаблоне(на контроллере) к "/img/**", то идёт путь в:                            для win 3 slashs
                .addResourceLocations("file:///" + uploadPath + "/");
                         //папка компа или сервера.    //в appllication.properties
    }
}
