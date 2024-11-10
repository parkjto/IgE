import React from 'react';
import ImgChange from "./ImageChange";
import styles from './RenderLoggedOut.module.css';
import {Link} from 'react-router-dom';
// import logoimg from "../img/android-chrome-192x192.png";

const LoggedOutUser = () => (
    <div className={styles.container}>
        <ImgChange/>
        <h1>오늘은 뭐를 먹어볼까나??</h1>
        <p><Link to="/login">로그인</Link> 후 메뉴를 추천 받으세요!</p>


    </div>
);

export default LoggedOutUser;
