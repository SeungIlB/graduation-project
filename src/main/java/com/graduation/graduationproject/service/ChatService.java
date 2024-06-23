package com.graduation.graduationproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graduation.graduationproject.config.websoket.WebSocketChatHandler;
import com.graduation.graduationproject.dto.ChatDTO;
import com.graduation.graduationproject.entity.ChatRoom;
import com.graduation.graduationproject.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final WebSocketChatHandler webSocketChatHandler;
    private final ObjectMapper objectMapper;

    public List<ChatRoom> findAll() {
        return chatRepository.findAll();
    }

    public Optional<ChatRoom> findRoomById(Long roomId) {
        return chatRepository.findById(roomId);
    }

    public ChatRoom createRoom(String predictedClass) {
        String roomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(roomId)
                .name(predictedClass)
                .build();
        ChatRoom savedChatRoom = chatRepository.save(chatRoom);
        log.info("Created chat room: {}", savedChatRoom);
        return savedChatRoom;
    }

    public void handleMessage(WebSocketSession session, ChatDTO chatMessageDto) {
        Optional<ChatRoom> roomOpt = chatRepository.findByRoomId(chatMessageDto.getRoomId());

        if (roomOpt.isPresent()) {
            ChatRoom room = roomOpt.get();
            Set<WebSocketSession> chatRoomSessions = room.getSessions();

            if (chatMessageDto.getType() == ChatDTO.MessageType.ENTER) {
                room.join(session);
                chatMessageDto.setMessage(chatMessageDto.getSender() + "님이 입장했습니다.");
            }

            TextMessage textMessage = new TextMessage(chatMessageDto.getMessage());
            chatRoomSessions.forEach(s -> sendMessage(s, textMessage));
        }
    }

    public void sendMessage(WebSocketSession session, TextMessage message) {
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            log.error("Error while sending message: ", e);
        }
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            String payload = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(payload));
        } catch (IOException e) {
            log.error("Error while sending message: ", e);
        }
    }
}