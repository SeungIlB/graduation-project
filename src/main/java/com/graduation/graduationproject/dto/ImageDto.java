package com.graduation.graduationproject.dto;

import lombok.Setter;
import lombok.Getter;
@Getter
@Setter
public class ImageDto {
    private Long id;
    private String filename;

    private String season;

    private String predictedClass;

    private String filepath; // 파일 절대 경로 필드 추가
}
