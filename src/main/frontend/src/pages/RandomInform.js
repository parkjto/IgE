import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ImageChange from "./ImageChange";
import style from './RandomInform.module.css';

const RandomMenu = () => {
    const [menu, setMenu] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchRandomMenu = () => {
        axios.get('http://localhost:8081/api/menus/random')
            .then(response => {
                console.log(response.data);
                if (response.data && response.data.menu_name) {
                    setMenu(response.data);
                } else {
                    setMenu({});
                }
                setLoading(false);
            })
            .catch(error => {
                setError(error);
                setLoading(false);
            });
    };

    useEffect(() => {
        // 컴포넌트가 처음 렌더링될 때 2초 동안 로딩 후 메뉴 가져오기
        const loadingTimeout = setTimeout(() => {
            fetchRandomMenu();
        }, 1300);

        return () => clearTimeout(loadingTimeout);
    }, []);

    const handleMenuClick = () => {
        setLoading(true);
        // 2초 동안 로딩 상태 유지 후 fetchRandomMenu 호출
        setTimeout(() => {
            fetchRandomMenu();
        }, 1300);
    };

    if (loading) {
        return <ImageChange />;  // 로딩 중에는 ImageChange를 보여줌
    }

    if (error) {
        return <p>Error: {error.message}</p>;
    }

    return (
        <div className={style.container}>
            <p className={style.text} onClick={handleMenuClick}>
                {menu.menu_name || "메뉴를 불러올 수 없습니다"}
            </p>
        </div>
    );
};

export default RandomMenu;
