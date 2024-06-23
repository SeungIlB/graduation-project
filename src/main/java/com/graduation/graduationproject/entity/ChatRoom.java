package com.graduation.graduationproject.entity;

import com.graduation.graduationproject.dto.ChatDTO;
import com.graduation.graduationproject.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roomId;

    @Column(nullable = false)
    private String name;

    @Transient
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handleAction(WebSocketSession session, ChatDTO message, ChatService service) {
        if (message.getType().equals(ChatDTO.MessageType.ENTER)) {
            join(session);
            message.setMessage(message.getSender() + " 님이 입장하셨습니다");
            sendMessage(message, service);
        } else if (message.getType().equals(ChatDTO.MessageType.TALK)) {
            sendMessage(message, service);
        }
    }

    public void join(WebSocketSession session) {
        sessions.add(session);
    }

    public <T> void sendMessage(T message, ChatService service) {
        sessions.parallelStream().forEach(session -> service.sendMessage(session, message));
    }

    public static ChatRoom of(String roomId, String name) {
        return ChatRoom.builder()
                .roomId(roomId)
                .name(name)
                .build();
    }
}