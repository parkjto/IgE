import React, { useEffect, useRef, useState } from "react";

const KakaoMapTest = ({ userPosition, clickedRestaurant, isMapVisible }) => {
    const mapRef = useRef(null);
    const [map, setMap] = useState(null);
    const [currentMarker, setCurrentMarker] = useState(null); // 현재 위치 마커
    const [restaurantMarkers, setRestaurantMarkers] = useState([]); // 클릭된 식당 마커들
    const [regionName, setRegionName] = useState(""); // 행정구역 이름 저장

    // 행정구역 이름 가져오기 함수
    const getRegionName = (latitude, longitude) => {
        const kakao = window.kakao; // Kakao API 로드
        const geocoder = new kakao.maps.services.Geocoder();

        geocoder.coord2RegionCode(longitude, latitude, (result, status) => {
            if (status === kakao.maps.services.Status.OK) {
                const region = result.find((item) => item.region_type === "H"); // 'H': 행정동
                if (region) {
                    setRegionName(region.address_name); // 행정구역 이름 저장
                    console.log("현재 위치의 행정구역 이름:", region.address_name);
                }
            } else {
                console.error("Geocoding 실패:", status);
            }
        });
    };

    // 지도 초기화
    useEffect(() => {
        if (userPosition && !map) {
            const script = document.createElement("script");
            script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=3abe3146cdb7a57e6e03b6ca6e652183&libraries=services&autoload=false`;
            script.async = true;
            document.body.appendChild(script);

            script.onload = () => {
                window.kakao.maps.load(() => {
                    const container = document.getElementById("map");
                    const options = {
                        center: new window.kakao.maps.LatLng(userPosition.latitude, userPosition.longitude),
                        level: 4,
                    };

                    const kakaoMap = new window.kakao.maps.Map(container, options);
                    setMap(kakaoMap);

                    // 현재 위치 기본 마커 생성
                    const currentPositionMarker = new window.kakao.maps.Marker({
                        map: kakaoMap,
                        position: new window.kakao.maps.LatLng(userPosition.latitude, userPosition.longitude),
                        title: "현재 위치",
                    });

                    setCurrentMarker(currentPositionMarker);

                    // 줌 컨트롤 추가
                    const zoomControl = new window.kakao.maps.ZoomControl();
                    kakaoMap.addControl(zoomControl, window.kakao.maps.ControlPosition.RIGHT);

                    // 지도 초기화 완료 후 행정구역 이름 가져오기
                    getRegionName(userPosition.latitude, userPosition.longitude);
                });
            };
        }
    }, [userPosition, map]);

    // 클릭된 식당에 마커 표시 및 중심 이동
    useEffect(() => {
        if (clickedRestaurant && map) {
            const latitude = parseFloat(clickedRestaurant.latitude) / 10000000;
            const longitude = parseFloat(clickedRestaurant.longitude) / 10000000;

            console.log("클릭된 식당 좌표:", { latitude, longitude });

            // 기존 식당 마커들을 제거
            restaurantMarkers.forEach((marker) => marker.setMap(null));

            const restaurantPosition = new window.kakao.maps.LatLng(latitude, longitude);

            // 새로운 식당 마커 생성
            const restaurantMarker = new window.kakao.maps.Marker({
                map: map,
                position: restaurantPosition,
                title: clickedRestaurant.title,
            });

            // 새로운 마커를 배열로 상태에 저장
            setRestaurantMarkers([restaurantMarker]);

            // 지도 중심을 클릭된 식당으로 이동
            map.setCenter(restaurantPosition);
        }
    }, [clickedRestaurant, map]);

    // 모달, 탭 등에서 지도 영역이 갱신되었음을 알림
    useEffect(() => {
        if (map && isMapVisible) {
            const timeoutId = setTimeout(() => {
                map.relayout();
                if (userPosition) {
                    map.setCenter(
                        new window.kakao.maps.LatLng(userPosition.latitude, userPosition.longitude)
                    );
                }
            }, 500); // 딜레이를 약간 늘려 브라우저 업데이트 시간 확보
            return () => clearTimeout(timeoutId); // 메모리 누수 방지
        }
    }, [isMapVisible, map, userPosition]);

    return (
        <div>
            <div id="map" style={{ width: "100%", height: "400px" }}></div>
            <p>현재 위치: {regionName}</p>
        </div>
    );
};

export default KakaoMapTest;
