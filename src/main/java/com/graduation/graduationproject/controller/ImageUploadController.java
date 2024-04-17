package com.graduation.graduationproject.controller;

import com.graduation.graduationproject.dto.AuthDto;
import com.graduation.graduationproject.service.UserService;
import com.graduation.graduationproject.service.VisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class ImageUploadController {

    private final VisionService googleVisionService;
    @Autowired
    private UserService userService;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public ImageUploadController(VisionService googleVisionService, BCryptPasswordEncoder encoder) {
        this.googleVisionService = googleVisionService;
        this.encoder = encoder;
    }

    @GetMapping("/upload")
    public String upload(){
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
    public ResponseEntity<String> saveLabel(@RequestParam("selectedLabel")  @PathVariable Long userId, String selectedLabel, AuthDto.UpdateDto updateDto) {
        // 사용자가 선택한 라벨을 사용자 정보에 저장
        updateDto.setLabel(selectedLabel);

        // 사용자 정보 업데이트 또는 저장 등의 작업 수행
        try {
            // 사용자 정보 업데이트 서비스 호출
            String encodedPassword = encoder.encode(updateDto.getPassword()); // 비밀번호 암호화
            userService.updateUser(userId, updateDto,encodedPassword);
            return new ResponseEntity<>("Label saved successfully.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to save label.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
