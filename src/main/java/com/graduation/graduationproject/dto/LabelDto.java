package com.graduation.graduationproject.dto;

import lombok.Getter;
import lombok.Setter;

public class LabelDto {

    @Getter
    @Setter
    public static class SaveLabelDto {
        private Long id;
        private String labelValue1;
        private String labelValue2;
        private String labelValue3;
    }

    @Getter
    @Setter
    public static class UpdateLabelDto{
        private Long id;
        private String labelValue1;
        private String labelValue2;
        private String labelValue3;
    }
}