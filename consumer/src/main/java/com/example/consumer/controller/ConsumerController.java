package com.example.consumer.controller;

import com.example.consumer.entity.Message;
import com.example.consumer.model.MessageDto;
import com.example.consumer.repository.MessageRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class ConsumerController {

    private final MessageRepository messageRepository;

    @PostMapping
    public ResponseEntity<?> processMessage(@Valid @RequestBody MessageDto request) {
        if (messageRepository.existsById(request.id())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Message message = new Message(request.id(), request.text());
        messageRepository.save(message);
        System.out.println(message.getText());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
