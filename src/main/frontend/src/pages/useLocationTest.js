const useLocation = () => {
    const [userPosition, setUserPosition] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        const getGeoLocation = async () => {
            try {
                // 먼저 IP 위치 요청
                const ipResponse = await axios.get("https://ipapi.co/json/");
                const { latitude: ipLatitude, longitude: ipLongitude, accuracy: ipAccuracy } = ipResponse.data;
                const ipPosition = {
                    latitude: ipLatitude,
                    longitude: ipLongitude,
                    accuracy: ipAccuracy || 10000, // 기본 정확도
                };

                console.log("IP 기반 위치:", ipPosition);

                // Geolocation API로 위치 가져오기
                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(
                        (position) => {
                            const { latitude, longitude, accuracy } = position.coords;
                            const geoPosition = { latitude, longitude, accuracy };
                            // 정확도가 더 높은 위치를 선택
                            if (geoPosition.accuracy <= ipPosition.accuracy) {
                                setUserPosition(geoPosition);
                            } else {
                                setUserPosition(ipPosition);
                            }
                        },
                        (err) => {
                            console.error("Geolocation error:", err);
                            setUserPosition(ipPosition); // Geolocation 실패 시 IP 위치 사용
                        },
                        {
                            enableHighAccuracy: true,
                            timeout: 10000,
                            maximumAge: 0,
                        }
                    );
                } else {
                    // Geolocation을 지원하지 않는 경우 IP 위치 사용
                    setUserPosition(ipPosition);
                }
            } catch (error) {
                console.error("IP 위치 오류:", error);
                setError("위치를 가져오는 데 실패했습니다.");
            }
        };

        getGeoLocation();
    }, []); // 최초 한번만 실행

    return { userPosition, error };
};
