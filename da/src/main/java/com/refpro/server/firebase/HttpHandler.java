package com.refpro.server.firebase;

import com.refpro.server.DTOs.FirebaseMessageDTO;
import com.refpro.server.DTOs.FirebaseTopicMessageDTO;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class HttpHandler {
    private static String FIREBASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY = "AAAAjh3KD7U:APA91bHcDRfM4Vk96KnYf2TA_AagYbyB2Y23iRvahJEuF5mgsooL--JN7FYN4UPyisZVizN5lIB5Jl768AqiDc0ex_vbZtfg9qNxJHT8n91I8nt2t94UYjx6uJrViLps0d7jC7dB-m1k";
    private RestTemplate restTemplate = new RestTemplate();
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(HttpHandler.class.getName());

    public void sendMessageToSingleDevice(String token, Map<String, String> data) {
        FirebaseMessageDTO message = new FirebaseMessageDTO();

        message.setTo(token);
        message.setData(data);

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer: " + SERVER_KEY);
        HttpEntity<FirebaseMessageDTO> entity = new HttpEntity<>(message, headers);


        ResponseEntity<String> response = restTemplate
                .exchange(FIREBASE_URL, HttpMethod.POST, entity, String.class);

        log.info(String.valueOf(response.getStatusCodeValue()));
    }

    public void sendMessageToTopic(String topic,  Map<String, String> data) {
        FirebaseMessageDTO message = new FirebaseMessageDTO();

        message.setTo("/topics/" + topic);
        message.setData(data);

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer: " + SERVER_KEY);
        HttpEntity<FirebaseMessageDTO> entity = new HttpEntity<>(message, headers);


        ResponseEntity<String> response = restTemplate
                .exchange(FIREBASE_URL, HttpMethod.POST, entity, String.class);

        log.info(String.valueOf(response.getStatusCodeValue()));
    }

    public void sendMessageToTopics(String topic1,String topic2 , Map<String, String> data) {
        FirebaseTopicMessageDTO message = new FirebaseTopicMessageDTO();

        message.setCondition(topic1 + "||" + topic2);
        message.setData(data);

        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer: " + SERVER_KEY);
        HttpEntity<FirebaseTopicMessageDTO> entity = new HttpEntity<>(message, headers);


        ResponseEntity<String> response = restTemplate
                .exchange(FIREBASE_URL, HttpMethod.POST, entity, String.class);

        log.info(String.valueOf(response.getStatusCodeValue()));
    }
}
