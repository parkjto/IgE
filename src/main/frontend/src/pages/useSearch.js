import { useState, useEffect } from "react";
import axios from "axios";
import { API_ENDPOINTS } from '../config/api';
import { calculateDistance, isValidCoordinate } from '../utils/distance';

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
            const response = await axios.get(API_ENDPOINTS.SEARCH_NAVER(query));
            const data = response.data || [];

            if (data.length === 0) {
                setError("검색 결과가 없습니다.");
            } else {
                const validResults = data.map((restaurant) => {
                    const latitude = parseFloat(restaurant.latitude);
                    const longitude = parseFloat(restaurant.longitude);

                    if (!isValidCoordinate(latitude, longitude)) {
                        if (process.env.NODE_ENV === 'development') {
                            console.error("잘못된 좌표 데이터:", {
                                latitude: restaurant.latitude,
                                longitude: restaurant.longitude,
                            });
                        }
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
            if (process.env.NODE_ENV === 'development') {
                console.error("API 요청 실패:", err);
            }
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
