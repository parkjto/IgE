/**
 * allergies 상수 테스트
 */
import { ALLERGY_TYPES, ALLERGY_LAYOUT, MIN_PASSWORD_LENGTH } from '../allergies';

describe('allergies 상수', () => {
    describe('ALLERGY_TYPES', () => {
        test('모든 알레르기 유형이 포함되어 있어야 함', () => {
            expect(ALLERGY_TYPES).toContain('계란');
            expect(ALLERGY_TYPES).toContain('콩');
            expect(ALLERGY_TYPES).toContain('우유');
            expect(ALLERGY_TYPES).toContain('밀');
            expect(ALLERGY_TYPES).toContain('땅콩');
            expect(ALLERGY_TYPES).toContain('생선');
            expect(ALLERGY_TYPES).toContain('갑각류');
            expect(ALLERGY_TYPES).toContain('견과류');
        });

        test('알레르기 유형은 8개여야 함', () => {
            expect(ALLERGY_TYPES.length).toBe(8);
        });
    });

    describe('ALLERGY_LAYOUT', () => {
        test('레이아웃은 2차원 배열이어야 함', () => {
            expect(Array.isArray(ALLERGY_LAYOUT)).toBe(true);
            expect(Array.isArray(ALLERGY_LAYOUT[0])).toBe(true);
        });

        test('각 행은 2개의 알레르기를 포함해야 함', () => {
            ALLERGY_LAYOUT.forEach(row => {
                expect(row.length).toBe(2);
            });
        });

        test('총 4개의 행이 있어야 함', () => {
            expect(ALLERGY_LAYOUT.length).toBe(4);
        });

        test('모든 알레르기 유형이 레이아웃에 포함되어 있어야 함', () => {
            const layoutAllergies = ALLERGY_LAYOUT.flat();
            ALLERGY_TYPES.forEach(allergy => {
                expect(layoutAllergies).toContain(allergy);
            });
        });
    });

    describe('MIN_PASSWORD_LENGTH', () => {
        test('비밀번호 최소 길이는 4여야 함', () => {
            expect(MIN_PASSWORD_LENGTH).toBe(4);
        });

        test('비밀번호 최소 길이는 양수여야 함', () => {
            expect(MIN_PASSWORD_LENGTH).toBeGreaterThan(0);
        });
    });
});





























