package com.graduation.graduationproject.service;

import com.graduation.graduationproject.entity.Label;
import com.graduation.graduationproject.repository.LabelRepository;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SimilarityService {

    @Autowired
    private LabelRepository labelRepository;

    public List<Long> recommendSimilarUsers(Long userId) {
        Label userLabel = labelRepository.findByUserId(userId);
        List<Label> allLabels = labelRepository.findAllExceptUserId(userId);

        // 사용자 레이블 값 추출
        String userLabelValue = getUserLabelValue(userLabel);

        List<Long> similarUserIds = findSimilarUsers(userLabelValue, allLabels);
        return similarUserIds;
    }

    private String getUserLabelValue(Label label) {
        return label.getLabelValue1() + label.getLabelValue2() + label.getLabelValue3();
    }

    private List<Long> findSimilarUsers(String userLabelValue, List<Label> allLabels) {
        List<Long> similarUserIds = new ArrayList<>();
        CosineSimilarity cosineSimilarity = new CosineSimilarity();

        for (Label label : allLabels) {
            String otherUserLabelValue = getLabelValue(label);
            double similarity = cosineSimilarity(userLabelValue, otherUserLabelValue);
            // 코사인 유사도가 일정 임계값 이상인 경우에만 유사한 사용자로 간주한다.
            if (similarity > 0.7) { // 임계값 설정 (예시로 0.7)
                similarUserIds.add(label.getId());
            }
        }

        return similarUserIds;
    }

    private String getLabelValue(Label label) {
        return label.getLabelValue1() + label.getLabelValue2() + label.getLabelValue3();
    }

    private double cosineSimilarity(String text1, String text2) {
        Map<Character, Integer> vector1 = getCharacterVector(text1);
        Map<Character, Integer> vector2 = getCharacterVector(text2);

        double dotProduct = 0;
        double norm1 = 0;
        double norm2 = 0;

        for (Map.Entry<Character, Integer> entry : vector1.entrySet()) {
            Character character = entry.getKey();
            int freq1 = entry.getValue();
            int freq2 = vector2.getOrDefault(character, 0);

            dotProduct += freq1 * freq2;
            norm1 += Math.pow(freq1, 2);
        }

        for (Map.Entry<Character, Integer> entry : vector2.entrySet()) {
            norm2 += Math.pow(entry.getValue(), 2);
        }

        double similarity = dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
        return similarity;
    }

    private Map<Character, Integer> getCharacterVector(String text) {
        Map<Character, Integer> vector = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            vector.put(c, vector.getOrDefault(c, 0) + 1);
        }
        return vector;
    }
}