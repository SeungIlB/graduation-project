package com.graduation.graduationproject.config.websoket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.graduationproject.dto.ChatDTO;
import org.springframework.web.socket.TextMessage;

public class Util {
    public static class Chat {
        private static final ObjectMapper objectMapper = new ObjectMapper();

        public static TextMessage resolveTextMessage(ChatDTO message) {
            try {
                return new TextMessage(objectMapper.writeValueAsString(message));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        public static ChatDTO resolvePayload(String payload) {
            try {
                return objectMapper.readValue(payload, ChatDTO.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}