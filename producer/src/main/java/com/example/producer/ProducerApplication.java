package com.example.producer;

import com.example.producer.model.Message;
import com.example.producer.service.ProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@SpringBootApplication
public class ProducerApplication implements CommandLineRunner {

    private final ProducerService producerService;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplicationBuilder()
                .sources(ProducerApplication.class)
                .web(WebApplicationType.NONE).build();

        springApplication.run(args).close();
    }

    @Override
    public void run(String... args) {
        Message message = new Message(1L, "Hello, world!");
        producerService.sendMessage(message);
    }

}
