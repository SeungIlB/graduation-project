package com.graduation.graduationproject.entity;

import com.graduation.graduationproject.dto.AuthDto;
import com.graduation.graduationproject.dto.LabelDto;
import com.graduation.graduationproject.dto.Role;
import lombok.*;

import javax.persistence.*;

@Entity(name = "label")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Label {


    @Id
    @Column(name = "user_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId //@MapsId 는 @id로 지정한 컬럼에 @OneToOne 이나 @ManyToOne 관계를 매핑시키는 역할
    @JoinColumn(name = "user_id")
    private User user;

    private String labelValue1;
    private String labelValue2;
    private String labelValue3;

    public static Label saveLabel(LabelDto.SaveLabelDto labelDto, User user) {
        Label label = new Label();
        label.setUser(user);
        label.labelValue1 = labelDto.getLabelValue1();
        label.labelValue2 = labelDto.getLabelValue2();
        label.labelValue3 = labelDto.getLabelValue3();

        return label;
    }

}