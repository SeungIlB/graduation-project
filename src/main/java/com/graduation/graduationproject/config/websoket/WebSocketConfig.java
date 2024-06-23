package com.graduation.graduationproject.config;

import com.graduation.graduationproject.config.websoket.WebSocketChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketChatHandler webSocketChatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketChatHandler, "/ws/chat")
                .addInterceptors(httpSessionHandshakeInterceptor())
                .setAllowedOrigins("*");
    }

    @Bean
    public HttpSessionHandshakeInterceptor httpSessionHandshakeInterceptor() {
        return new HttpSessionHandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(
                    org.springframework.http.server.ServerHttpRequest request,
                    org.springframework.http.server.ServerHttpResponse response,
                    org.springframework.web.socket.WebSocketHandler wsHandler,
                    java.util.Map<String, Object> attributes) throws java.lang.Exception {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated()) {
                    return false;
                }
                return super.beforeHandshake(request, response, wsHandler, attributes);
            }
        };
    }
}