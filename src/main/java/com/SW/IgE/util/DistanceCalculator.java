package com.SW.IgE.util;

/**
 * 거리 계산 유틸리티 클래스
 * Haversine 공식을 사용하여 두 지점 간의 거리를 계산합니다.
 */
public final class DistanceCalculator {

    private static final int EARTH_RADIUS_KM = 6371; // 지구 반지름 (km)

    private DistanceCalculator() {
        // 인스턴스화 방지
    }

    /**
     * 두 좌표 간의 거리를 계산 (Haversine 공식)
     *
     * @param lat1 첫 번째 지점의 위도
     * @param lon1 첫 번째 지점의 경도
     * @param lat2 두 번째 지점의 위도
     * @param lon2 두 번째 지점의 경도
     * @return 두 지점 간의 거리 (km)
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * 좌표 유효성 검사
     *
     * @param latitude 위도
     * @param longitude 경도
     * @return 유효한 좌표이면 true
     */
    public static boolean isValidCoordinate(double latitude, double longitude) {
        return !Double.isNaN(latitude) &&
                !Double.isNaN(longitude) &&
                latitude >= -90 && latitude <= 90 &&
                longitude >= -180 && longitude <= 180;
    }
}





























