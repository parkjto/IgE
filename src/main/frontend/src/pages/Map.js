import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';

// 네이버 맵 API 스크립트를 로드하는 컴포넌트
function LoadNaverMapScript() {
    useEffect(() => {
        // 스크립트 요소 생성
        const script = document.createElement('script');
        // 네이버 지도 API의 스크립트 URL을 설정, 환경 변수로 API 키를 사용
        script.src = `https://openapi.map.naver.com/openapi/v3/maps.js?ncpClientId=${process.env.REACT_APP_MAP_CLIENT_KEY}`;
        script.async = true; // 스크립트 비동기 로드 설정

        // 스크립트가 로드되었을 때 콜백 함수 추가
        script.onload = () => {
            console.log('Naver Maps API 로드 완료');
        };

        // 스크립트를 document의 head에 추가
        document.head.appendChild(script);

        // 컴포넌트가 언마운트될 때 스크립트를 제거
        return () => {
            document.head.removeChild(script);
        };
    }, []); // 빈 배열로 useEffect가 첫 렌더링 시 한 번만 실행되도록 설정

    return null; // 이 컴포넌트는 UI를 렌더링하지 않음
}

// 지도 컴포넌트
function Map() {
    const [map, setMap] = useState(null); // 맵 상태 변수 선언
    const [marker, setMarker] = useState(null); // 마커 상태 변수 선언
    const [currentLocation, setCurrentLocation] = useState(null); // 현재 위치 상태 변수 선언
    const [error, setError] = useState(null); // 오류 상태 변수 선언

    useEffect(() => {
        // Geolocation API 옵션 설정
        const geolocationOptions = {
            enableHighAccuracy: true, // 고정밀 모드 활성화
            timeout: 10000, // 최대 대기 시간 10초
            maximumAge: 0, // 캐시된 위치 정보 사용하지 않음
        };

        // 위치 추적을 위한 watchPosition 사용
        let watchId;

        if (navigator.geolocation) {
            // 위치 추적 시작
            watchId = navigator.geolocation.watchPosition(
                position => {
                    const { latitude, longitude } = position.coords; // 위도 및 경도 추출
                    setCurrentLocation({ lat: latitude, lng: longitude }); // 현재 위치 상태 업데이트
                },
                error => {
                    console.error('Geolocation error:', error);
                    setError(error.message); // 오류 메시지 상태 업데이트
                },
                geolocationOptions
            );
        } else {
            // 브라우저가 Geolocation API를 지원하지 않는 경우 경고 로그 출력
            console.error('Geolocation을 지원하지 않는 브라우저입니다.');
            setError('Geolocation을 지원하지 않는 브라우저입니다.'); // 오류 메시지 상태 업데이트
        }

        // 컴포넌트 언마운트 시 위치 추적 중지
        return () => {
            if (watchId !== undefined) {
                navigator.geolocation.clearWatch(watchId); // 위치 추적 중지
            }
        };
    }, []); // 빈 배열로 컴포넌트가 마운트될 때 한 번만 실행되도록 설정

    useEffect(() => {
        // 네이버 맵 API가 로드되고, 사용자의 위치 정보가 설정된 경우에만 실행
        if (!window.naver || !currentLocation) return;

        // 맵이 아직 초기화되지 않은 경우에만 초기화
        if (!map) {
            // 네이버 맵 객체 생성, 중앙 좌표를 사용자의 현재 위치로 설정
            const naverMap = new window.naver.maps.Map('map', {
                center: new window.naver.maps.LatLng(currentLocation.lat, currentLocation.lng), // 초기 중심 좌표
                zoom: 17, // 줌 레벨 설정
                minZoom: 15, // 최소 줌 레벨 설정
                maxZoom: 19, // 최대 줌 레벨 설정 (필요에 따라 조정 가능)
            });

            // 현재 위치에 마커를 추가하여 시각적으로 표시
            const naverMarker = new window.naver.maps.Marker({
                position: new window.naver.maps.LatLng(currentLocation.lat, currentLocation.lng), // 마커 위치
                map: naverMap, // 마커가 표시될 맵
                title: '현재 위치', // 마커에 대한 타이틀
            });

            // 맵과 마커 객체를 상태에 저장
            setMap(naverMap);
            setMarker(naverMarker);
        } else {
            // 맵이 이미 초기화된 경우, 중심 좌표와 마커 위치를 업데이트
            const newLatLng = new window.naver.maps.LatLng(currentLocation.lat, currentLocation.lng);
            map.setCenter(newLatLng); // 맵의 중심을 새로운 위치로 이동

            if (marker) {
                marker.setPosition(newLatLng); // 마커의 위치를 새로운 위치로 이동
            } else {
                // 마커가 아직 생성되지 않은 경우 새로 생성
                const naverMarker = new window.naver.maps.Marker({
                    position: newLatLng,
                    map: map,
                    title: '현재 위치',
                });
                setMarker(naverMarker); // 새 마커 상태 업데이트
            }
        }
    }, [currentLocation, map, marker]); // currentLocation, map, marker 상태가 변경될 때마다 실행

    // 오류가 발생한 경우 사용자에게 알림 표시
    if (error) {
        return (
            <ErrorContainer>
                <p>현재 위치를 가져올 수 없습니다: {error}</p> {/* 오류 메시지 표시 */}
            </ErrorContainer>
        );
    }

    // 위치 정보가 아직 로드되지 않은 경우 로딩 메시지 표시
    if (!currentLocation) {
        return (
            <LoadingContainer>
                <p>현재 위치를 가져오는 중...</p> {/* 로딩 메시지 표시 */}
            </LoadingContainer>
        );
    }

    // 지도 컨테이너를 렌더링
    return <MapContainer id="map"></MapContainer>;
}

// 박스 컨테이너 스타일 설정
const BoxContainer = styled.div`
    width: 500px; /* 박스의 너비 설정 */
    height: 500px; /* 박스의 높이 설정 */
    margin: 10% auto 0 5%; /* 위 | 오른쪽 | 아래 | 왼쪽 */
    border: 1px solid black; /* 박스의 테두리 설정 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 박스에 그림자 효과 추가 */
    border-radius: 10px; /* 박스의 모서리를 둥글게 설정 */
    overflow: hidden; /* 내용이 박스 밖으로 넘치지 않도록 설정 */
    position: relative; /* 내부 요소의 위치 설정을 위해 relative 설정 */
`;

// 지도 컨테이너 스타일 설정
const MapContainer = styled.div`
    height: 100%; /* 컨테이너 높이를 부모 요소(BoxContainer)의 100%로 설정 */
    width: 100%; /* 컨테이너 너비를 부모 요소(BoxContainer)의 100%로 설정 */
`;

// 로딩 상태를 위한 스타일 설정
const LoadingContainer = styled.div`
    width: 500px; /* 로딩 컨테이너 너비 설정 */
    height: 500px; /* 로딩 컨테이너 높이 설정 */
    display: flex; /* flexbox 레이아웃 설정 */
    justify-content: center; /* 가로 방향 중앙 정렬 */
    align-items: center; /* 세로 방향 중앙 정렬 */
    border: 1px solid #ddd; /* 경계선 스타일 설정 */
    border-radius: 8px; /* 모서리 둥글게 설정 */
    background-color: #f9f9f9; /* 배경 색상 설정 */
`;

// 오류 상태를 위한 스타일 설정
const ErrorContainer = styled.div`
    width: 500px; /* 오류 컨테이너 너비 설정 */
    height: 500px; /* 오류 컨테이너 높이 설정 */
    display: flex; /* flexbox 레이아웃 설정 */
    justify-content: center; /* 가로 방향 중앙 정렬 */
    align-items: center; /* 세로 방향 중앙 정렬 */
    border: 1px solid #ff4d4f; /* 경계선 스타일 설정 */
    border-radius: 8px; /* 모서리 둥글게 설정 */
    background-color: #ffe6e6; /* 배경 색상 설정 */
    color: #ff4d4f; /* 텍스트 색상 설정 */
`;

// 최상위 컴포넌트 (App)
function App() {
    return (
        <div>
            <Link to="/main">MAIN</Link> {/* 메인 페이지로 이동하는 링크 */}
            <>
                <LoadNaverMapScript /> {/* 네이버 맵 API 스크립트를 로드 */}
                <BoxContainer>
                    <Map /> {/* 박스 안에 맵을 표시 */}
                </BoxContainer>
            </>
        </div>
    );
}

export default App; // 기본 내보내기
