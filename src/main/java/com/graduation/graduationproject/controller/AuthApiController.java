package com.graduation.graduationproject.controller;

import com.graduation.graduationproject.dto.AuthDto;
import com.graduation.graduationproject.dto.ImageDto;
import com.graduation.graduationproject.dto.UserDTO;
import com.graduation.graduationproject.entity.Image;
import com.graduation.graduationproject.entity.User;
import com.graduation.graduationproject.repository.UserDetailsImpl;
import com.graduation.graduationproject.repository.UserRepository;
import com.graduation.graduationproject.service.AuthService;
import com.graduation.graduationproject.service.ImageService;
import com.graduation.graduationproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApiController {
    private static final Logger log = LoggerFactory.getLogger(ImageController.class);
    private final AuthService authService;
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;
    private final ImageService imageService;

    private final long COOKIE_EXPIRATION = 7776000; // 90일

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid AuthDto.SignupDto signupDto) {
        String encodedPassword = encoder.encode(signupDto.getPassword()); // 비밀번호 암호화
        AuthDto.SignupDto newSignupDto = AuthDto.SignupDto.encodePassword(signupDto, encodedPassword);

        userService.registerUser(newSignupDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 로그인 -> 토큰 발급
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDto.LoginDto loginDto) {
        // User 등록 및 Refresh Token 저장
        AuthDto.TokenDto tokenDto = authService.login(loginDto);
        Long userid = userService.findIdByUsername(loginDto.getUsername());

        // RT 저장
        HttpCookie httpCookie = ResponseCookie.from("refresh-token", tokenDto.getRefreshToken())
                .maxAge(COOKIE_EXPIRATION)
                .httpOnly(true)
                .secure(true)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
                // AT 저장
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken()).body(Map.of(
                        "accessToken", tokenDto.getAccessToken(),
                        "userid", userid));
    }
    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody AuthDto.UpdateDto updateDto) {

        try {
            String encodedPassword = encoder.encode(updateDto.getPassword()); // 비밀번호 암호화
            userService.updateUser(userId, updateDto,encodedPassword);
            return ResponseEntity.ok("사용자 정보가 성공적으로 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("사용자가 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String requestAccessToken) {
        if (!authService.validate(requestAccessToken)) {
            return ResponseEntity.status(HttpStatus.OK).build(); // 재발급 필요X
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 재발급 필요
        }
    }
    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue(name = "refresh-token") String requestRefreshToken,
                                     @RequestHeader("Authorization") String requestAccessToken) {
        AuthDto.TokenDto reissuedTokenDto = authService.reissue(requestAccessToken, requestRefreshToken);

        if (reissuedTokenDto != null) { // 토큰 재발급 성공
            // RT 저장
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", reissuedTokenDto.getRefreshToken())
                    .maxAge(COOKIE_EXPIRATION)
                    .httpOnly(true)
                    .secure(true)
                    .build();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    // AT 저장
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + reissuedTokenDto.getAccessToken())
                    .build();

        } else { // Refresh Token 탈취 가능성
            // Cookie 삭제 후 재로그인 유도
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                    .maxAge(0)
                    .path("/")
                    .build();
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .build();
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken) {
        authService.logout(requestAccessToken);
        ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

    @GetMapping("/mypage/{userId}")
    public ResponseEntity<?> loadMyPage(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 현재 로그인한 사용자의 ID 가져오기
        Long loggedInUserId = userService.getLoggedInUserId(userDetails);

        // 요청한 사용자와 현재 로그인한 사용자의 ID가 일치하지 않는 경우 권한이 없는 것으로 처리
        if (!userId.equals(loggedInUserId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // userId를 이용하여 사용자 정보를 데이터베이스에서 조회합니다.
        User user = userService.getUserById(userId);

        // 사용자 정보를 DTO로 변환합니다.
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setName(user.getName());
        userDTO.setNickname(user.getNickname());
        userDTO.setAddress(user.getAddress());
        userDTO.setBirthdate(user.getBirthdate());
        userDTO.setPhone(user.getPhone());

        // 사용자 정보가 존재하지 않는 경우 404 에러를 반환합니다.
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // 사용자 정보가 존재하는 경우 해당 정보를 반환합니다.
        return ResponseEntity.ok(userDTO);
    }
    @GetMapping("/images/{userId}")
    public ResponseEntity<?> getImagesByUserId(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            log.warn("UserDetails is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long loggedInUserId = userService.getLoggedInUserId(userDetails);
        log.debug("Logged in user ID: {}", loggedInUserId);

        if (!userId.equals(loggedInUserId)) {
            log.warn("User ID mismatch: requested {}, logged in {}", userId, loggedInUserId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ImageDto> images = imageService.getImageDtosByUserId(userId);
        if (images.isEmpty()) {
            log.warn("No images found for user ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(images);
    }
    @GetMapping("/images/compare")
    public ResponseEntity<?> comparePredictedClass(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Long userId = userDetails.getId();
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        String predictedClass = user.getImages().isEmpty() ? null : user.getImages().get(0).getPredictedClass();
        if (predictedClass == null) {
            return ResponseEntity.status(404).body("Predicted class not found for the user");
        }

        List<Image> matchingImages = imageService.findByPredictedClass(predictedClass);
        List<ImageDto> imageDtos = matchingImages.stream()
                .filter(img -> !img.getUser().getId().equals(userId))
                .map(img -> {
                    ImageDto dto = new ImageDto();
                    dto.setId(img.getId());
                    dto.setFilename(img.getFilename());
                    dto.setSeason(img.getSeason());
                    dto.setPredictedClass(img.getPredictedClass());
                    dto.setFilepath(img.getFilepath());
                    dto.setNickname(img.getUser().getNickname()); // Assuming ImageDto has a nickname field
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(imageDtos);
    }
}
