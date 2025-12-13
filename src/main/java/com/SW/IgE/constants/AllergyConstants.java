package com.SW.IgE.constants;

import java.util.Arrays;
import java.util.List;

/**
 * 알레르기 관련 상수 클래스
 */
public final class AllergyConstants {

    private AllergyConstants() {
        // 인스턴스화 방지
    }

    /**
     * 알레르기 유발 성분 목록
     */
    public static final List<String> ALLERGY_TYPES = Arrays.asList(
            "계란", "콩", "우유", "밀", "땅콩", "생선", "갑각류", "견과류"
    );

    /**
     * 비밀번호 최소 길이
     */
    public static final int MIN_PASSWORD_LENGTH = 4;

    /**
     * 알레르기 유형이 유효한지 검증
     * @param allergy 알레르기 유형
     * @return 유효한 알레르기 유형이면 true
     */
    public static boolean isValidAllergyType(String allergy) {
        return ALLERGY_TYPES.contains(allergy);
    }
}





























