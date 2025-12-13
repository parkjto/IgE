package com.SW.IgE.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DistanceCalculator 테스트")
class DistanceCalculatorTest {

    // 서울 좌표 (위도, 경도)
    private static final double SEOUL_LAT = 37.5665;
    private static final double SEOUL_LON = 126.9780;

    // 부산 좌표 (위도, 경도)
    private static final double BUSAN_LAT = 35.1796;
    private static final double BUSAN_LON = 129.0756;

    // 서울-부산 간 대략적인 거리 (km) - 실제 거리는 약 325km
    private static final double EXPECTED_DISTANCE_KM = 325.0;
    private static final double DISTANCE_TOLERANCE = 10.0; // 허용 오차 10km

    @Test
    @DisplayName("서울과 부산 간 거리 계산 - 정상적인 경우")
    void calculateDistance_SeoulToBusan_ReturnsApproximateDistance() {
        // when
        double distance = DistanceCalculator.calculateDistance(
                SEOUL_LAT, SEOUL_LON,
                BUSAN_LAT, BUSAN_LON
        );

        // then
        assertTrue(distance > 0);
        // 실제 거리와 약 10km 이내의 오차 허용
        assertTrue(Math.abs(distance - EXPECTED_DISTANCE_KM) < DISTANCE_TOLERANCE,
                String.format("예상 거리: %.2fkm, 실제 거리: %.2fkm", EXPECTED_DISTANCE_KM, distance));
    }

    @Test
    @DisplayName("같은 지점 간 거리 계산 - 0 반환")
    void calculateDistance_SamePoint_ReturnsZero() {
        // when
        double distance = DistanceCalculator.calculateDistance(
                SEOUL_LAT, SEOUL_LON,
                SEOUL_LAT, SEOUL_LON
        );

        // then
        assertEquals(0.0, distance, 0.01);
    }

    @Test
    @DisplayName("짧은 거리 계산 - 정상적인 경우")
    void calculateDistance_ShortDistance_ReturnsCorrectDistance() {
        // given - 서울 시청과 명동 간 거리 (약 1km)
        double seoulCityHallLat = 37.5665;
        double seoulCityHallLon = 126.9780;
        double myeongdongLat = 37.5636;
        double myeongdongLon = 126.9827;

        // when
        double distance = DistanceCalculator.calculateDistance(
                seoulCityHallLat, seoulCityHallLon,
                myeongdongLat, myeongdongLon
        );

        // then
        assertTrue(distance > 0);
        assertTrue(distance < 5.0); // 5km 이내
    }

    @Test
    @DisplayName("좌표 유효성 검사 - 유효한 좌표")
    void isValidCoordinate_ValidCoordinates_ReturnsTrue() {
        // when & then
        assertTrue(DistanceCalculator.isValidCoordinate(SEOUL_LAT, SEOUL_LON));
        assertTrue(DistanceCalculator.isValidCoordinate(0.0, 0.0));
        assertTrue(DistanceCalculator.isValidCoordinate(90.0, 180.0));
        assertTrue(DistanceCalculator.isValidCoordinate(-90.0, -180.0));
    }

    @Test
    @DisplayName("좌표 유효성 검사 - 위도 범위 초과")
    void isValidCoordinate_LatitudeOutOfRange_ReturnsFalse() {
        // when & then
        assertFalse(DistanceCalculator.isValidCoordinate(91.0, 0.0));
        assertFalse(DistanceCalculator.isValidCoordinate(-91.0, 0.0));
    }

    @Test
    @DisplayName("좌표 유효성 검사 - 경도 범위 초과")
    void isValidCoordinate_LongitudeOutOfRange_ReturnsFalse() {
        // when & then
        assertFalse(DistanceCalculator.isValidCoordinate(0.0, 181.0));
        assertFalse(DistanceCalculator.isValidCoordinate(0.0, -181.0));
    }

    @Test
    @DisplayName("좌표 유효성 검사 - NaN 값")
    void isValidCoordinate_NaNValues_ReturnsFalse() {
        // when & then
        assertFalse(DistanceCalculator.isValidCoordinate(Double.NaN, 0.0));
        assertFalse(DistanceCalculator.isValidCoordinate(0.0, Double.NaN));
        assertFalse(DistanceCalculator.isValidCoordinate(Double.NaN, Double.NaN));
    }
}





























