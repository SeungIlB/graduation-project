package com.graduation.graduationproject.dto;

import lombok.*;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ImageDto {
    private Long id;
    private String filename;

    private String season;

    private String predictedClass;

    private String filepath; // 파일 절대 경로 필드 추가
    private String nickname; // 사용자 닉네임 추가

}
