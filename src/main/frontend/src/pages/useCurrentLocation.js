import { useState, useEffect } from "react";

const useCurrentLocation = () => {
    const [userPosition, setUserPosition] = useState(null);
    const [error, setError] = useState("");

    useEffect(() => {
        const getCurrentLocation = () => {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(
                    (position) => {
                        const { latitude, longitude } = position.coords;
                        setUserPosition({ latitude, longitude });
                    },
                    (err) => {
                        console.error("현재 위치 가져오기 오류", err);
                        setError("현재 위치를 가져오는 데 실패했습니다.");
                    }
                );
            } else {
                setError("Geolocation API를 지원하지 않습니다.");
            }
        };

        getCurrentLocation();
    }, []);

    return { userPosition, error };
};

export default useCurrentLocation;
