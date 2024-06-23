package com.graduation.graduationproject.config.websoket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.graduationproject.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;

    // 현재 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Principal principal = session.getPrincipal();
        if (principal != null && principal.getName() != null) {
            log.info("User {} connected", principal.getName());
            sessions.add(session);
        } else {
            session.close();
            log.warn("Connection closed due to unauthorized access");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        Principal principal = session.getPrincipal();
        if (principal != null && principal.getName() != null) {
            ChatDTO chatMessageDto = mapper.readValue(payload, ChatDTO.class);
            // handle message logic here
        } else {
            session.close();
            log.warn("Message handling failed due to unauthorized access");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} disconnected", session.getId());
        sessions.remove(session);
    }

    public void sendMessage(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            log.error("Error while sending message: ", e);
        }
    }
}