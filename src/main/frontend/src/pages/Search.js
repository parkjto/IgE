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
        handleSearch,
    } = useSearch(query, userPosition);

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

            {searchError && <div className={styles.errorMessage}>{searchError}</div>}

            {locationError && <div className={styles.errorMessage}>{locationError}</div>}

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
                        onClick={() => {
                            setClickedRestaurant(restaurant);
                        }}
                    >
                        <strong>{restaurant.title.replace(/<[^>]+>/g, "")}</strong>
                        <p>{restaurant.address}</p>
                        {restaurant.distance && typeof restaurant.distance === "number" ? (
                            <p>현재 위치와의 거리: {restaurant.distance.toFixed(2)} km</p>
                        ) : (
                            <p>위치 정보를 확인할 수 없습니다.</p>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Search;
