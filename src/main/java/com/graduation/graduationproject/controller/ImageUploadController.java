package com.graduation.graduationproject.controller;


import com.graduation.graduationproject.dto.LabelDto;
import com.graduation.graduationproject.entity.Label;
import com.graduation.graduationproject.entity.User;
import com.graduation.graduationproject.repository.LabelRepository;
import com.graduation.graduationproject.service.LabelService;
import com.graduation.graduationproject.service.UserService;
import com.graduation.graduationproject.service.VisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
public class ImageUploadController {

    private final VisionService googleVisionService;
    @Autowired
    private UserService userService;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    private LabelService labelService;
    private LabelRepository labelRepository;

    @Autowired
    public ImageUploadController(VisionService googleVisionService, BCryptPasswordEncoder encoder) {
        this.googleVisionService = googleVisionService;
        this.encoder = encoder;
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload.html";
    }

    @GetMapping
    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please select a file to upload.", HttpStatus.BAD_REQUEST);
        }

        try {
            // 이미지 파일을 바이트 배열로 변환하여 Google Vision API로 전송하여 라벨 분석
            byte[] imageBytes = file.getBytes();
            List<String> labels = googleVisionService.detectLabels(imageBytes);

            // 상위 3개의 라벨을 추출하여 모델에 추가
            model.addAttribute("labels", labels.subList(0, Math.min(labels.size(), 3)));

            // 사용자에게 선택한 라벨을 입력받을 수 있는 폼 페이지로 이동
            return ResponseEntity.ok("selectLabel"); // 리다이렉트를 위해 실제 URL 주소를 반환
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to process image.", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @PostMapping("/saveLabel/{userId}")
    public ResponseEntity<String> saveLabel(@PathVariable("userId") Long userId,
                                            @RequestBody LabelDto.SaveLabelDto labelDto) {
        User user = userService.findUserById(userId); // 사용자 ID로 사용자 검색
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + userId);
        }

        labelDto.setId(userId); // userId를 경로 변수로 설정

        labelService.saveLabel(labelDto, user); // 사용자 ID 전달

        return ResponseEntity.status(HttpStatus.CREATED).body("Label values saved successfully.");
    }

    @PutMapping("/updateLabel/{userId}/{labelId}")
    public ResponseEntity<String> updateLabel(@PathVariable Long userId,
                                              @PathVariable Long labelId,
                                              @RequestBody LabelDto.UpdateLabelDto labelDto) {
        // 여기서 userId와 labelId를 사용하여 라벨을 찾고, 그 라벨을 업데이트합니다.
        // 이 부분은 실제 비즈니스 로직에 따라 구현되어야 합니다.

        // LabelService를 사용하여 라벨을 업데이트합니다.
        labelService.updateLabel(userId, labelId, labelDto);

        // 업데이트가 성공했음을 응답합니다.
        return ResponseEntity.ok("Label updated successfully");
    }

    @DeleteMapping("/deleteLabel/{userId}/{labelId}")
    public ResponseEntity<String> deleteLabel(@PathVariable Long userId,
                                              @PathVariable Long labelId) {
        // LabelService를 사용하여 라벨을 삭제합니다.
        labelService.deleteLabel(userId, labelId);

        // 삭제가 성공했음을 응답합니다.
        return ResponseEntity.ok("Label deleted successfully");
    }

}