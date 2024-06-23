package com.graduation.graduationproject.config.websoket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.WebSocketHandler;



@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class SpringConfig implements WebSocketConfigurer {

    // WebSocketHandler 에 관한 생성자 추가
    private final WebSocketHandler webSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // endpoint 설정 : /ws/chat
        // 이를 통해서 ws://localhost:8080/ws/chat 으로 요청이 들어오면 websocket 통신을 진행한다.
        registry.addHandler(webSocketHandler, "/ws/chat").setAllowedOrigins("*");
    }
}
