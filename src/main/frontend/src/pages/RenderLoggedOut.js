import React from 'react';
import ImgChange from "./ImageChange";
import styles from './RenderLoggedOut.module.css';
import { Link } from 'react-router-dom';
import InformIgE from "./InformIgE";  // 알림 컴포넌트

const RenderLoggedOutUser = () => (
    <div className={styles.Informcontainer}>
        {/* 로그인하지 않은 상태 전달 */}
        <InformIgE isLoggedIn={false} />
        <div className={styles.container}>
            <ImgChange /> {/* 이미지 변경 컴포넌트 */}
            <h1>오늘은 뭐를 먹어볼까나??</h1>
            <p><Link to="/login" className={styles.loginLink}>로그인</Link> 후 메뉴를 추천 받으세요!</p> {/* 로그인 링크 스타일링 추가 */}
        </div>
    </div>
);

export default RenderLoggedOutUser;
