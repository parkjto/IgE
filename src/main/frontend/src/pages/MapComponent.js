// import React, { useEffect } from 'react';
//
// const MapComponent = () => {
//     useEffect(() => {
//         // 카카오 맵 스크립트 로드
//         const loadKakaoMapScript = () => {
//             const script = document.createElement('script');
//             script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${window.KAKAO_MAP_CLIENT_ID}&autoload=false`;
//             script.async = true;
//             document.head.appendChild(script);
//
//             script.onload = () => {
//                 // 카카오 맵 초기화
//                 window.kakao.maps.load(() => {
//                     console.log("카카오 맵 로딩 완료");
//                     // 기본적으로 첫 번째 장소 설정
//                     const mapContainer = document.getElementById('map');
//                     const mapOption = {
//                         center: new window.kakao.maps.LatLng(37.5665, 126.9780), // 서울 시청 위치
//                         level: 3,
//                     };
//
//                     const map = new window.kakao.maps.Map(mapContainer, mapOption);
//
//                     // 예시로 하나의 마커 추가
//                     new window.kakao.maps.Marker({
//                         map: map,
//                         position: new window.kakao.maps.LatLng(37.5665, 126.9780), // 서울 시청 위치
//                     });
//                 });
//             };
//         };
//
//         loadKakaoMapScript();
//     }, []);
//
//     return (
//         <div>
//             <div id="map" style={{ width: '100%', height: '400px' }}></div>
//         </div>
//     );
// };
//
// export default MapComponent;
