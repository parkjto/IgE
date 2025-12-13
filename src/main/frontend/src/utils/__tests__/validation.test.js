/**
 * validation 유틸리티 함수 테스트
 */
import { validateEmail, validatePassword, validatePasswordConfirm, validateAge } from '../validation';

describe('validation 유틸리티 함수', () => {
    describe('validateEmail', () => {
        test('유효한 이메일 형식은 true를 반환해야 함', () => {
            expect(validateEmail('test@example.com')).toBe(true);
            expect(validateEmail('user.name@domain.co.kr')).toBe(true);
            expect(validateEmail('test123@test-domain.com')).toBe(true);
        });

        test('유효하지 않은 이메일 형식은 false를 반환해야 함', () => {
            expect(validateEmail('invalid-email')).toBe(false);
            expect(validateEmail('test@')).toBe(false);
            expect(validateEmail('@example.com')).toBe(false);
            expect(validateEmail('test@example')).toBe(false);
            expect(validateEmail('')).toBe(false);
        });
    });

    describe('validatePassword', () => {
        test('유효한 비밀번호는 isValid가 true여야 함', () => {
            const result = validatePassword('password123');
            expect(result.isValid).toBe(true);
            expect(result.message).toBe('');
        });

        test('비밀번호가 없으면 isValid가 false여야 함', () => {
            const result = validatePassword('');
            expect(result.isValid).toBe(false);
            expect(result.message).toContain('비밀번호를 입력해주세요');
        });

        test('비밀번호가 최소 길이보다 짧으면 isValid가 false여야 함', () => {
            const result = validatePassword('123');
            expect(result.isValid).toBe(false);
            expect(result.message).toContain('최소');
        });

        test('커스텀 최소 길이를 사용할 수 있어야 함', () => {
            const result = validatePassword('12345', 6);
            expect(result.isValid).toBe(false);
            expect(result.message).toContain('6자');
        });
    });

    describe('validatePasswordConfirm', () => {
        test('비밀번호가 일치하면 isValid가 true여야 함', () => {
            const result = validatePasswordConfirm('password123', 'password123');
            expect(result.isValid).toBe(true);
            expect(result.message).toBe('');
        });

        test('비밀번호가 일치하지 않으면 isValid가 false여야 함', () => {
            const result = validatePasswordConfirm('password123', 'password456');
            expect(result.isValid).toBe(false);
            expect(result.message).toContain('일치하지 않습니다');
        });
    });

    describe('validateAge', () => {
        test('유효한 나이는 isValid가 true여야 함', () => {
            expect(validateAge(25).isValid).toBe(true);
            expect(validateAge('30').isValid).toBe(true);
            expect(validateAge(1).isValid).toBe(true);
            expect(validateAge(150).isValid).toBe(true);
        });

        test('나이가 없으면 isValid가 false여야 함', () => {
            const result = validateAge('');
            expect(result.isValid).toBe(false);
            expect(result.message).toContain('나이를 입력해주세요');
        });

        test('나이가 범위를 벗어나면 isValid가 false여야 함', () => {
            expect(validateAge(0).isValid).toBe(false);
            expect(validateAge(151).isValid).toBe(false);
            expect(validateAge(-1).isValid).toBe(false);
        });

        test('숫자가 아닌 값은 isValid가 false여야 함', () => {
            expect(validateAge('abc').isValid).toBe(false);
            expect(validateAge(NaN).isValid).toBe(false);
        });
    });
});





























