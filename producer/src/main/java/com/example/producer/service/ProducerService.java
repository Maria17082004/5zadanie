package com.example.producer.service;

import com.example.producer.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final RestTemplate restTemplate;

    @Value("${producer.consumer.url}")
    private String consumerUrl;

    public void sendMessage(Message message) {
        // Отправляем сообщение в consumer микросервис
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Message> requestEntity = new HttpEntity<>(message, headers);

            // Добавляем timeout на запрос, чтобы избежать блокировки в случае сбоев сети
            restTemplate.postForObject(consumerUrl, requestEntity, Void.class);
        } catch (RestClientException e) {
            // Если запрос не удалось отправить, повторяем несколько раз (retry)
            int retriesLeft = 3;
            while (retriesLeft > 0) {
                System.out.println("Failed to send message: " + e.getMessage());
                retriesLeft--;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}

                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity<Message> requestEntity = new HttpEntity<>(message, headers);

                    restTemplate.postForObject(consumerUrl, requestEntity, Void.class);
                    break;
                } catch (RestClientException e1) {
                    e = e1;
                }
            }

            if (retriesLeft == 0) {
                System.out.println("Failed to send message after multiple retries: " + e.getMessage());
            }
        }
    }
}
