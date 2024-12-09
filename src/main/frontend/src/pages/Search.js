import React, { useState ,useEffect} from "react";
import styles from "./Search.module.css";
import useCurrentLocation from "./useCurrentLocation";
import useSearch from "./useSearch";
import KakaoMapTest from "./KakaoMapTest";
import Header from "./Header";
import { useLocation } from "react-router-dom";
// import RandomMenu from "./RandomMenu"; // RandomMenu 컴포넌트 추가


const Search = ({user, onLogout}) => {
    const [query, setQuery] = useState("");
    const location = useLocation(); // location 객체 가져오기

    // RandomMenu에서 전달된 menu_name을 query에 초기값으로 설정
    useEffect(() => {
        if (location.state?.menuName) {
            setQuery(location.state.menuName);
        }
    }, [location.state]);

    const { userPosition, error: locationError } = useCurrentLocation();
    const {
        results,
        setResults,
        clickedRestaurant,
        loading,
        error: searchError,
        setClickedRestaurant,
        handleSearch,
    } = useSearch(query, userPosition, 3);

    // const handleRandomMenuSelect = (menuName) => {
    //     setQuery(menuName);
    // };

    return (
        <>
        <Header user={user} onLogout={onLogout} />
        <div className={styles.container}>

            <h1 className={styles.title}>근처 식당 검색</h1>
            <p style={{color: 'rgba(128, 128, 128, 0.7)', fontSize: '12px'}}>
                검색어를 조금 더 간단히 수정해 보세요!
            </p>
            <p style={{color: 'rgba(128, 128, 128, 0.7)', fontSize: '14px', marginTop : '10px'}}>
               ( 예시: "냉이순두부찌개" → "순두부찌개")
            </p>
            <p style={{color: 'rgba(128, 128, 128, 0.7)', fontSize: '12px', marginTop : '10px', marginBottom : '10px'}}>
                이런 식으로 수정하면 더 정확한 검색 결과를 얻을 수 있어요.
            </p>

            <div className={styles.searchContainer}>
                <input
                    type="text"
                    value={query} // 전달받은 값을 검색창에 반영
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
        </>
    );
};

export default Search;
