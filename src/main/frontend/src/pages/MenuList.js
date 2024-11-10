import React, { useEffect, useState } from 'react';
import axios from 'axios';
import styles from './MenuList.module.css';

const MenuList = ({ userData }) => {
    const [menus, setMenus] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [activeTab, setActiveTab] = useState("recommended"); // 탭 상태 추가

    useEffect(() => {
        const fetchMenus = async () => {
            try {
                const response = await axios.get('http://localhost:8081/api/menus');
                setMenus(response.data);
            } catch (err) {
                setError(`Failed to load menus: ${err.response ? err.response.data.message : err.message}`);
            } finally {
                setLoading(false);
            }
        };

        fetchMenus();
    }, []);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    const userAllergies = userData?.allergies || [];

    const filteredMenus = menus.filter(menu =>
        menu.ingredients.every(ingredient => !userAllergies.includes(ingredient))
    );

    const nonRecommendedMenus = menus.filter(menu =>
        menu.ingredients.some(ingredient => userAllergies.includes(ingredient))
    );

    return (
        <div className={styles.container}>
            <div className={styles.tabContainer}>
                <div
                    className={`${styles.tab} ${activeTab === "recommended" ? styles.active : ""}`}
                    onClick={() => setActiveTab("recommended")}
                >
                    추천 메뉴
                </div>
                <div
                    className={`${styles.tab} ${activeTab === "nonRecommended" ? styles.active : ""}`}
                    onClick={() => setActiveTab("nonRecommended")}
                >
                    비추천 메뉴
                </div>
            </div>

            {activeTab === "recommended" && (

                    <ul className={styles.menuList}>
                        {filteredMenus.length > 0 ? filteredMenus.map((menu) => (
                            <div className={styles.menuListBox}>
                                <li key={menu.id} className={styles.menuItem}>
                                    <h3 className={styles.menuName}>{menu.menu_name}</h3>
                                    <p className={styles.menuIngredients}>재료: {menu.ingredients.join(', ')}</p>

                                </li>
                            </div>
                        )) : <p>알레르기를 고려한 추천 메뉴가 없습니다.</p>}
                    </ul>

            )}

            {activeTab === "nonRecommended" && (
                <ul className={styles.menuList}>
                    {nonRecommendedMenus.length > 0 ? nonRecommendedMenus.map((menu) => (
                        <li key={menu.id} className={styles.menuItem}>
                            <h3 className={styles.menuName}>{menu.menu_name}</h3>
                            <p className={styles.menuIngredients}>재료: {menu.ingredients.join(', ')}</p>
                            <p className={styles.nonRecommended}>추천하지 않아요</p>
                            <hr />
                        </li>
                    )) : <p>비추천 메뉴가 없습니다.</p>}
                </ul>
            )}
        </div>
    );
};

export default MenuList;
