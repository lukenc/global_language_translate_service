package com.global.language.web_content_translate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication
@MapperScan("com.global.language.web_content_translate.repository")
public class WebContentTranslateApplication implements CommandLineRunner {
    @Autowired
     DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(WebContentTranslateApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {

        System.out.println("DATASOURCE = " + dataSource);

    }

}
