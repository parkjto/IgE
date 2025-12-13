/**
 * 거리 계산 유틸리티 함수
 * Haversine 공식을 사용하여 두 지점 간의 거리를 계산합니다.
 */

/**
 * 두 좌표 간의 거리를 계산 (Haversine 공식)
 * @param {number} lat1 - 첫 번째 지점의 위도
 * @param {number} lon1 - 첫 번째 지점의 경도
 * @param {number} lat2 - 두 번째 지점의 위도
 * @param {number} lon2 - 두 번째 지점의 경도
 * @returns {number} - 두 지점 간의 거리 (km, 소수점 2자리)
 */
export const calculateDistance = (lat1, lon1, lat2, lon2) => {
    const toRad = (value) => (value * Math.PI) / 180;
    const R = 6371; // 지구 반지름 (km)

    const dLat = toRad(lat2 - lat1);
    const dLon = toRad(lon2 - lon1);
    
    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);
    
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    
    return parseFloat((R * c).toFixed(2));
};

/**
 * 좌표 유효성 검사
 * @param {number} latitude - 위도
 * @param {number} longitude - 경도
 * @returns {boolean} - 유효한 좌표이면 true
 */
export const isValidCoordinate = (latitude, longitude) => {
    return !isNaN(latitude) && 
           !isNaN(longitude) && 
           latitude >= -90 && 
           latitude <= 90 && 
           longitude >= -180 && 
           longitude <= 180;
};





























