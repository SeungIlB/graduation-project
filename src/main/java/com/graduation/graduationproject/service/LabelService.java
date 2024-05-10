package com.graduation.graduationproject.service;

import com.graduation.graduationproject.dto.AuthDto;
import com.graduation.graduationproject.dto.LabelDto;
import com.graduation.graduationproject.entity.Label;
import com.graduation.graduationproject.entity.User;
import com.graduation.graduationproject.repository.LabelRepository;
import com.graduation.graduationproject.repository.UserRepository;
import io.grpc.internal.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.graduation.graduationproject.dto.LabelDto.SaveLabelDto;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LabelService {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveLabel(LabelDto.SaveLabelDto labelDto, User user) {
        Label label = Label.saveLabel(labelDto, user);

        labelRepository.save(label);
    }

    @Transactional
    public void updateLabel(Long userId, Long labelId, LabelDto.UpdateLabelDto labelDto) {
        // 사용자 ID로 사용자를 찾습니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // 라벨 ID로 라벨을 찾습니다.
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new RuntimeException("Label not found with id: " + labelId));

        // 라벨을 업데이트합니다.
        label.setLabelValue1(labelDto.getLabelValue1());
        label.setLabelValue2(labelDto.getLabelValue2());
        label.setLabelValue3(labelDto.getLabelValue3());

        // 사용자와 연관된 라벨인지 확인합니다.
        if (!label.getUser().getId().equals(userId)) {
            throw new RuntimeException("Label does not belong to user with id: " + userId);
        }

        // 업데이트된 라벨을 저장합니다.
        labelRepository.save(label);
    }

    public void deleteLabel(Long userId, Long labelId) {
        // 사용자 ID로 사용자를 찾습니다.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // 라벨 ID로 라벨을 찾습니다.
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new RuntimeException("Label not found with id: " + labelId));

        // 라벨을 삭제합니다.
        labelRepository.delete(label);
    }
    public boolean checkIfUserIdExists(Long userId) {
        // 주어진 userId 값을 사용하여 레코드를 조회합니다.
        Label label = labelRepository.findByUserId(userId);

        // 조회된 레코드가 null이면 해당 userId가 존재하지 않으므로 false 반환
        if (label == null) {
            return false;
        }
        // 조회된 레코드가 null이 아니면 해당 userId가 존재하므로 true 반환
        return true;
    }
}
