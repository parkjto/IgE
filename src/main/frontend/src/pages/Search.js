import React, { useState } from "react";
import styles from "./Search.module.css";
import useCurrentLocation from "./useCurrentLocation";
import useSearch from "./useSearch";
import KakaoMapTest from "./KakaoMapTest";

const Search = () => {
    const [query, setQuery] = useState("");
    const { userPosition, error: locationError } = useCurrentLocation();
    const {
        results,
        clickedRestaurant,
        loading,
        error: searchError,
        setClickedRestaurant,
        restaurantDistance, // 거리 정보
        handleSearch,
    } = useSearch(query, userPosition);

    // 오류 타입별 스타일 및 아이콘
    const getErrorStyle = (errorType) => {
        const errorStyles = {
            location: styles.locationError,
            query: styles.queryError,
            noResults: styles.noResultsError,
            network: styles.networkError,
        };
        return errorStyles[errorType] || styles.defaultError;
    };

    console.log("현재 Query 상태:", query); // 디버깅 추가
    console.log("현재 위치 상태:", userPosition); // 디버깅 추가

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>근처 식당 검색</h1>

            <div className={styles.searchContainer}>
                <input
                    type="text"
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    placeholder="식당 이름이나 메뉴를 입력하세요"
                    className={styles.searchInput}
                />
                <button
                    className={styles.searchButton}
                    onClick={handleSearch}
                    disabled={loading}
                >
                    {loading ? "검색 중..." : "검색"}
                </button>
            </div>

            {searchError && (
                <div className={`${styles.errorMessage} ${getErrorStyle(searchError.type)}`}>
                    {searchError}
                </div>
            )}

            <KakaoMapTest userPosition={userPosition} clickedRestaurant={clickedRestaurant} />

            <div className={styles.searchResults}>
                {results.length === 0 && !loading && (
                    <div className={styles.noResults}>
                        {searchError || "검색 결과가 없습니다."}
                    </div>
                )}
                {results.map((restaurant, index) => (
                    <div
                        key={index}
                        className={styles.restaurantItem}
                        onClick={() => setClickedRestaurant(restaurant)}
                    >
                        <strong>{restaurant.title.replace(/<[^>]+>/g, "")}</strong>
                        <p>{restaurant.address}</p>
                        {restaurantDistance && (
                            <p>현재 위치와의 거리: {restaurantDistance.toFixed(2)} km</p>
                        )}

                    </div>
                ))}
            </div>
        </div>
    );
};

export default Search;
