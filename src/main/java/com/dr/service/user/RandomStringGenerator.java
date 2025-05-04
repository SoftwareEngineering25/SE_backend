package com.dr.service.user;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomStringGenerator {

    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?";

    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomString(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Length must be at least 8 characters.");
        }

        // 최소 하나씩 포함해야 할 문자들을 미리 추가
        StringBuilder result = new StringBuilder();
        result.append(UPPER_CASE.charAt(random.nextInt(UPPER_CASE.length())));
        result.append(LOWER_CASE.charAt(random.nextInt(LOWER_CASE.length())));
        result.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        result.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        // 나머지 랜덤 문자를 추가
        String allCharacters = UPPER_CASE + LOWER_CASE + DIGITS + SPECIAL_CHARACTERS;
        for (int i = 4; i < length; i++) {
            result.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        // 문자열을 섞어 보안 강화
        List<Character> shuffleResult = new ArrayList<>();
        for (char c : result.toString().toCharArray()) {
            shuffleResult.add(c);
        }
        Collections.shuffle(shuffleResult);

        // 최종 결과 문자열 생성
        StringBuilder finalResult = new StringBuilder();
        for (char c : shuffleResult) {
            finalResult.append(c);
        }

        return finalResult.toString();
    }

}

