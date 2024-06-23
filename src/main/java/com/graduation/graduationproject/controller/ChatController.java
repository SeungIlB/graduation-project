package com.graduation.graduationproject.controller;

import com.graduation.graduationproject.entity.ChatRoom;
import com.graduation.graduationproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService service;

    @PostMapping("/room")
    public ResponseEntity<ChatRoom> createOrGetRoom(@RequestParam String predictedClass){
        ChatRoom chatRoom = service.createOrGetRoomByPredictedClass(predictedClass);
        log.info("Chat room ID: {}, Name: {}", chatRoom.getRoomId(), chatRoom.getName());
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoom>> findAllRooms(){
        List<ChatRoom> rooms = service.findAllRooms();
        return ResponseEntity.ok(rooms);
    }
}