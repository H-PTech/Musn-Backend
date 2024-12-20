package com.hnptech.musn.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class NicknameGenerator {

    // 음악 관련 형용사 리스트
    private static final List<String> ADJECTIVES = Arrays.asList(
            "신나는", "감미로운", "폭발적인", "리드미컬한", "열정적인", "잔잔한", "파워풀한", "몽환적인"
    );

    // 음악 관련 명사 리스트
    private static final List<String> NOUNS = Arrays.asList(
            "가수", "작곡가", "연주자", "DJ", "보컬", "랩퍼", "기타리스트", "드러머"
    );

    private final Random random;

    public NicknameGenerator() {
        this.random = new Random();
    }

    /**
     * 닉네임을 생성하는 메서드
     * @return 형용사 + 명사의 조합으로 생성된 닉네임
     */
    public String generateNickname() {
        String adjective = ADJECTIVES.get(random.nextInt(ADJECTIVES.size()));
        String noun = NOUNS.get(random.nextInt(NOUNS.size()));
        return adjective + " " + noun;
    }

//    public static void main(String[] args) {
//        NicknameGenerator generator = new NicknameGenerator();
//
//        // 닉네임 5개 생성
//        for (int i = 0; i < 5; i++) {
//            System.out.println("생성된 닉네임: " + generator.generateNickname());
//        }
//    }
}
