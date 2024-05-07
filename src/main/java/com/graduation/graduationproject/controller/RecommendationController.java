package com.graduation.graduationproject.controller;

import com.graduation.graduationproject.entity.User;
import com.graduation.graduationproject.repository.UserDetailsImpl;
import com.graduation.graduationproject.service.SimilarityService;
import com.graduation.graduationproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired
    private SimilarityService similarityService;

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> recommendSimilarUsers(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long loggedInUserId = userService.getLoggedInUserId(userDetails);

        // 요청한 사용자와 현재 로그인한 사용자의 ID가 일치하지 않는 경우 권한이 없는 것으로 처리
        if (!userId.equals(loggedInUserId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Long> recommendedUsers = similarityService.recommendSimilarUsers(userId);
        if (recommendedUsers.isEmpty()) {
            return new ResponseEntity<>("유사한 사용자가 없습니다.", HttpStatus.NOT_FOUND);
        } else {
            List<Long> top5Users = recommendedUsers.subList(0, Math.min(recommendedUsers.size(), 5));
            return new ResponseEntity<>(top5Users, HttpStatus.OK);
        }
    }

    @GetMapping("/{userId}/info")
    public ResponseEntity<Object> recommendSimilarUserInfo(@PathVariable Long userId){
        // 특정 사용자의 정보를 가져오는 코드
        User user = userService.getUserById(userId);

        // 사용자 정보가 없는 경우
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // 사용자 정보가 있는 경우
        return ResponseEntity.ok(user);
    }
}