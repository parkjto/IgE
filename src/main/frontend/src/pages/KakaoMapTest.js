import React, { useEffect } from "react";

const KakaoMapTest = ({ userPosition, clickedRestaurant }) => {
    useEffect(() => {
        if (userPosition) {
            const script = document.createElement("script");
            script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=3abe3146cdb7a57e6e03b6ca6e652183&autoload=false`;
            script.async = true;
            document.body.appendChild(script);

            script.onload = () => {
                window.kakao.maps.load(() => {
                    const container = document.getElementById("map");
                    const options = {
                        center: new window.kakao.maps.LatLng(userPosition.latitude, userPosition.longitude),
                        level: 4, // 지도 확대 수준
                    };
                    const map = new window.kakao.maps.Map(container, options);

                    // 현재 위치 마커 추가
                    const userMarker = new window.kakao.maps.Marker({
                        map,
                        position: new window.kakao.maps.LatLng(userPosition.latitude, userPosition.longitude),
                        title: "현재 위치",
                    });

                    // 반경 1km 원 표시
                    const circle = new window.kakao.maps.Circle({
                        center: new window.kakao.maps.LatLng(userPosition.latitude, userPosition.longitude),
                        radius: 1000, // 반경 (미터 단위)
                        strokeWeight: 2,
                        strokeColor: "#FF0000",
                        strokeOpacity: 0.3,
                        strokeStyle: "solid",
                        fillColor: "#FF0000",
                        fillOpacity: 0.08,
                    });
                    circle.setMap(map);

                    // 클릭된 식당 마커 표시 (변환 없이 좌표 그대로 사용)
                    if (clickedRestaurant && clickedRestaurant.latitude && clickedRestaurant.longitude) {
                        const restaurantPosition = new window.kakao.maps.LatLng(
                            clickedRestaurant.latitude,
                            clickedRestaurant.longitude
                        );

                        const restaurantMarker = new window.kakao.maps.Marker({
                            map,
                            position: restaurantPosition,
                            title: clickedRestaurant.title,
                        });

                        // 지도 중심을 클릭된 식당 위치로 이동
                        map.setCenter(restaurantPosition);
                    } else {
                        console.error("식당 좌표가 유효하지 않거나 변환할 수 없습니다:", clickedRestaurant);
                    }
                });
            };
        }
    }, [userPosition, clickedRestaurant]);

    if (!userPosition) {
        return <div>위치를 확인 중입니다...</div>;
    }

    return (
        <div>
            <div id="map" style={{ width: "100%", height: "400px" }}></div>
        </div>
    );
};

export default KakaoMapTest;
