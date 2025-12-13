/**
 * 유효성 검사 유틸리티 함수
 */

/**
 * 이메일 형식 검증
 * @param {string} email - 검증할 이메일 주소
 * @returns {boolean} - 유효한 이메일 형식이면 true
 */
export const validateEmail = (email) => {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
};

/**
 * 비밀번호 유효성 검사
 * @param {string} password - 검증할 비밀번호
 * @param {number} minLength - 최소 길이 (기본값: 4)
 * @returns {{isValid: boolean, message: string}} - 검증 결과
 */
export const validatePassword = (password, minLength = 4) => {
    if (!password) {
        return { isValid: false, message: '비밀번호를 입력해주세요.' };
    }
    if (password.length < minLength) {
        return { 
            isValid: false, 
            message: `비밀번호는 최소 ${minLength}자 이상이어야 합니다.` 
        };
    }
    return { isValid: true, message: '' };
};

/**
 * 비밀번호 확인 검증
 * @param {string} password - 원본 비밀번호
 * @param {string} confirmPassword - 확인 비밀번호
 * @returns {{isValid: boolean, message: string}} - 검증 결과
 */
export const validatePasswordConfirm = (password, confirmPassword) => {
    if (password !== confirmPassword) {
        return { isValid: false, message: '비밀번호가 일치하지 않습니다.' };
    }
    return { isValid: true, message: '' };
};

/**
 * 나이 유효성 검사
 * @param {number|string} age - 검증할 나이
 * @returns {{isValid: boolean, message: string}} - 검증 결과
 */
export const validateAge = (age) => {
    const ageNum = typeof age === 'string' ? parseInt(age, 10) : age;
    if (!ageNum || isNaN(ageNum)) {
        return { isValid: false, message: '나이를 입력해주세요.' };
    }
    if (ageNum < 1 || ageNum > 150) {
        return { isValid: false, message: '올바른 나이를 입력해주세요.' };
    }
    return { isValid: true, message: '' };
};





























