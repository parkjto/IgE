/**
 * distance 유틸리티 함수 테스트
 */
import { calculateDistance, isValidCoordinate } from '../distance';

describe('distance 유틸리티 함수', () => {
    // 서울 좌표
    const seoulLat = 37.5665;
    const seoulLon = 126.9780;

    // 부산 좌표
    const busanLat = 35.1796;
    const busanLon = 129.0756;

    describe('calculateDistance', () => {
        test('서울과 부산 간 거리를 계산할 수 있어야 함', () => {
            const distance = calculateDistance(seoulLat, seoulLon, busanLat, busanLon);
            expect(distance).toBeGreaterThan(0);
            expect(distance).toBeLessThan(400); // 서울-부산은 약 325km
            expect(typeof distance).toBe('number');
        });

        test('같은 지점 간 거리는 0이어야 함', () => {
            const distance = calculateDistance(seoulLat, seoulLon, seoulLat, seoulLon);
            expect(distance).toBe(0);
        });

        test('짧은 거리를 정확히 계산할 수 있어야 함', () => {
            // 서울 시청과 명동 간 거리 (약 1km)
            const cityHallLat = 37.5665;
            const cityHallLon = 126.9780;
            const myeongdongLat = 37.5636;
            const myeongdongLon = 126.9827;

            const distance = calculateDistance(cityHallLat, cityHallLon, myeongdongLat, myeongdongLon);
            expect(distance).toBeGreaterThan(0);
            expect(distance).toBeLessThan(5); // 5km 이내
        });

        test('결과는 소수점 2자리까지 반환되어야 함', () => {
            const distance = calculateDistance(seoulLat, seoulLon, busanLat, busanLon);
            const decimalPlaces = (distance.toString().split('.')[1] || '').length;
            expect(decimalPlaces).toBeLessThanOrEqual(2);
        });
    });

    describe('isValidCoordinate', () => {
        test('유효한 좌표는 true를 반환해야 함', () => {
            expect(isValidCoordinate(seoulLat, seoulLon)).toBe(true);
            expect(isValidCoordinate(0, 0)).toBe(true);
            expect(isValidCoordinate(90, 180)).toBe(true);
            expect(isValidCoordinate(-90, -180)).toBe(true);
        });

        test('위도가 범위를 벗어나면 false를 반환해야 함', () => {
            expect(isValidCoordinate(91, 0)).toBe(false);
            expect(isValidCoordinate(-91, 0)).toBe(false);
        });

        test('경도가 범위를 벗어나면 false를 반환해야 함', () => {
            expect(isValidCoordinate(0, 181)).toBe(false);
            expect(isValidCoordinate(0, -181)).toBe(false);
        });

        test('NaN 값은 false를 반환해야 함', () => {
            expect(isValidCoordinate(NaN, 0)).toBe(false);
            expect(isValidCoordinate(0, NaN)).toBe(false);
            expect(isValidCoordinate(NaN, NaN)).toBe(false);
        });
    });
});





























