package com.SW.IgE.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AllergyConstants 테스트")
class AllergyConstantsTest {

    @Test
    @DisplayName("알레르기 유형 목록 확인")
    void allergyTypes_ShouldContainAllTypes() {
        // when & then
        assertEquals(8, AllergyConstants.ALLERGY_TYPES.size());
        assertTrue(AllergyConstants.ALLERGY_TYPES.contains("계란"));
        assertTrue(AllergyConstants.ALLERGY_TYPES.contains("콩"));
        assertTrue(AllergyConstants.ALLERGY_TYPES.contains("우유"));
        assertTrue(AllergyConstants.ALLERGY_TYPES.contains("밀"));
        assertTrue(AllergyConstants.ALLERGY_TYPES.contains("땅콩"));
        assertTrue(AllergyConstants.ALLERGY_TYPES.contains("생선"));
        assertTrue(AllergyConstants.ALLERGY_TYPES.contains("갑각류"));
        assertTrue(AllergyConstants.ALLERGY_TYPES.contains("견과류"));
    }

    @Test
    @DisplayName("알레르기 유형 검증 - 유효한 유형")
    void isValidAllergyType_ValidType_ReturnsTrue() {
        // when & then
        assertTrue(AllergyConstants.isValidAllergyType("계란"));
        assertTrue(AllergyConstants.isValidAllergyType("콩"));
        assertTrue(AllergyConstants.isValidAllergyType("우유"));
    }

    @Test
    @DisplayName("알레르기 유형 검증 - 유효하지 않은 유형")
    void isValidAllergyType_InvalidType_ReturnsFalse() {
        // when & then
        assertFalse(AllergyConstants.isValidAllergyType("토마토"));
        assertFalse(AllergyConstants.isValidAllergyType(""));
        assertFalse(AllergyConstants.isValidAllergyType(null));
    }

    @Test
    @DisplayName("비밀번호 최소 길이 확인")
    void minPasswordLength_ShouldBeFour() {
        // when & then
        assertEquals(4, AllergyConstants.MIN_PASSWORD_LENGTH);
        assertTrue(AllergyConstants.MIN_PASSWORD_LENGTH > 0);
    }
}





























