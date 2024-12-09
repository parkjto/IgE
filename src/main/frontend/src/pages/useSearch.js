import { useState, useEffect } from "react";
import axios from "axios";

// 거리 계산 함수 (Haversine 공식을 사용)
const calculateDistance = (lat1, lon1, lat2, lon2) => {
    const toRad = (value) => (value * Math.PI) / 180;
    const R = 6371; // 지구 반지름 (단위: km)

    const dLat = toRad(lat2 - lat1);
    const dLon = toRad(lon2 - lon1);
    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return parseFloat((R * c).toFixed(2)); // 소수점 2자리까지 반환
};

const useSearch = (query, userPosition, maxDistance = 3) =>  {
    const [results, setResults] = useState([]);
    const [clickedRestaurant, setClickedRestaurant] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [restaurantDistance, setRestaurantDistance] = useState(null);

    const handleSearch = async () => {
        if (!query) return;
        if (!userPosition) {
            setError("위치 정보를 먼저 확인해주세요.");
            return;
        }


        setLoading(true);
        setError("");
        try {
            const response = await axios.get(
                `http://localhost:8081/api/search/naver?query=${encodeURIComponent(query)}`
            );
            const data = response.data || [];

            if (data.length === 0) {
                setError("검색 결과가 없습니다.");
            } else {
                const validResults = data.map((restaurant) => {
                    const latitude = parseFloat(restaurant.latitude);
                    const longitude = parseFloat(restaurant.longitude);

                    if (isNaN(latitude) || isNaN(longitude)) {
                        console.error("잘못된 좌표 데이터:", {
                            latitude: restaurant.latitude,
                            longitude: restaurant.longitude,
                        });
                        return null; // 잘못된 데이터는 제외
                    }

                    return {
                        ...restaurant,
                        latitude,
                        longitude,
                    };
                }).filter(Boolean); // 유효한 데이터만 유지

                // 사용자 위치와의 거리 계산
                const resultsWithDistance = validResults.map((restaurant) => {
                    if (userPosition) {
                        const { latitude, longitude } = restaurant;
                        const distance = calculateDistance(
                            userPosition.latitude,
                            userPosition.longitude,
                            latitude,
                            longitude
                        );
                        return {
                            ...restaurant,
                            distance, // 거리 추가
                        };
                    }
                    return restaurant;
                });

                setResults(resultsWithDistance);
                setClickedRestaurant(resultsWithDistance[0]); // 첫 번째 결과 선택
            }
        } catch (err) {
            console.error("API 요청 실패:", err);
            setError("검색 중 문제가 발생했습니다.");
        } finally {
            setLoading(false);
        }
    };

    return {
        results,
        clickedRestaurant,
        loading,
        error,
        restaurantDistance,
        setClickedRestaurant,
        handleSearch,
    };
};

export default useSearch;
