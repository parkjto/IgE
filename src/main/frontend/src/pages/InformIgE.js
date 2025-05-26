import React, { useState, useEffect } from 'react';
import axios from 'axios';
import style from './InformIgE.module.css';
import { Link } from 'react-router-dom';

const InformIgE = ({ isLoggedIn, setIsLoggedIn }) => {
    const [informs, setInforms] = useState([]);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (isLoggedIn) {
            setLoading(true); // 로딩 상태 시작
            axios.get("http://localhost:8081/api/all-informs")
                .then(response => {
                    setInforms(response.data);
                    setLoading(false); // 로딩 완료
                })
                .catch(error => {
                    console.error("정보 가져오기 실패:", error);
                    setError("정보를 불러오는 데 실패했습니다.");
                    setLoading(false); // 로딩 완료
                });
        } else {
            setInforms([]); // 로그인하지 않았을 때 데이터를 초기화
        }
    }, [isLoggedIn]); // isLoggedIn이 변경될 때마다 실행

    // 인덱스 슬라이딩 처리
    useEffect(() => {
        if (isLoggedIn && informs.length > 0) {
            setCurrentIndex(0); // 새로 데이터를 받아왔을 때 인덱스를 초기화
            const interval = setInterval(() => {
                setCurrentIndex((prevIndex) =>
                    prevIndex + 1 < informs.length ? prevIndex + 1 : 0
                );
            }, 3000);
            return () => clearInterval(interval); // 클린업
        }
    }, [isLoggedIn, informs]); // 로그인 상태와 informs 배열에 변경이 있을 때마다 실행

    if (!isLoggedIn) {
        return (
            <div className={style.bubbleBox}>
                <div className={style.bubbleIcon}>💡</div>
                <div className={style.bubbleText}>
                    <p>🍕🍔🍟🥚🍞<Link to="/login" className={style.text}>로그인</Link> 후 메뉴를 추천 받으세요!🥪🍗🍖🍚🍝</p>
                </div>
            </div>
        );
    }

    return (
        <div className={style.bubbleBox}>
            <div className={style.bubbleIcon}>💡</div>
            <div className={style.bubbleText}>
                {loading
                    ? "정보를 가져오는 중..."
                    : error
                        ? error
                        : (informs.length > 0 ? informs[currentIndex].inform : "그거 아세요?? 귤에 붙어있는 하얀거 이름은 귤락 입니다.")
                }
            </div>
        </div>
    );
};

export default InformIgE;
