import { useState, useEffect } from "react";
import axios from "axios";

// 주소를 좌표로 변환하는 함수
const geocodeAddress = async (address) => {
    const geocoder = new window.kakao.maps.services.Geocoder();
    return new Promise((resolve, reject) => {
        geocoder.addressSearch(address, (result, status) => {
            if (status === window.kakao.maps.services.Status.OK) {
                let latitude = result[0].y; // 위도
                let longitude = result[0].x; // 경도
                latitude = latitude.toFixed(6);  // 소수점 6자리로 제한
                longitude = longitude.toFixed(6); // 소수점 6자리로 제한
                resolve({ latitude, longitude });
            } else {
                reject("주소 변환 실패");
            }
        });
    });
};



// 거리 계산 함수 (Haversine 공식을 사용)
const calculateDistance = (lat1, lon1, lat2, lon2) => {
    const R = 6371; // 지구 반지름 (km)
    const dLat = ((lat2 - lat1) * Math.PI) / 180;
    const dLon = ((lon2 - lon1) * Math.PI) / 180;
    const a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos((lat1 * Math.PI) / 180) *
        Math.cos((lat2 * Math.PI) / 180) *
        Math.sin(dLon / 2) *
        Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const distance = R * c; // 거리 (km)
    return distance.toFixed(2);  // 소수점 2자리로 제한
};


const useSearch = (query, userPosition) => {
    const [results, setResults] = useState([]);
    const [clickedRestaurant, setClickedRestaurant] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [restaurantDistance, setRestaurantDistance] = useState(null);

    useEffect(() => {
        if (clickedRestaurant && userPosition) {
            const calculateRestaurantDistance = async () => {
                try {
                    const { latitude, longitude } = await geocodeAddress(clickedRestaurant.address);
                    const distance = calculateDistance(
                        userPosition.latitude,
                        userPosition.longitude,
                        latitude,
                        longitude
                    );
                    setRestaurantDistance(distance);
                } catch (error) {
                    console.error("주소 변환 또는 거리 계산 오류:", error);
                }
            };

            calculateRestaurantDistance();
        }
    }, [clickedRestaurant, userPosition]);

    const handleSearch = async () => {
        if (!query.trim()) {
            alert("검색어를 입력해주세요.");
            return;
        }

        setLoading(true);
        setError("");
        try {
            const url = `http://localhost:8081/api/search/naver?query=${encodeURIComponent(query)}`;
            console.log("API 요청 URL:", url);
            const response = await axios.get(url);
            console.log("API 응답 데이터:", response.data);

            const data = response.data || [];
            if (data.length === 0) {
                setError("검색 결과가 없습니다.");
            } else {
                setResults(data);
                setClickedRestaurant(data[0]); // 기본으로 첫 번째 레스토랑 선택
            }
        } catch (error) {
            console.error("API 요청 에러:", error);
            setError("검색 중 오류가 발생했습니다.");
        } finally {
            setLoading(false);
        }
    };

    return {
        results,
        clickedRestaurant,
        loading,
        error,
        setClickedRestaurant,
        restaurantDistance, // 거리 정보 추가
        handleSearch,
    };
};

export default useSearch;
