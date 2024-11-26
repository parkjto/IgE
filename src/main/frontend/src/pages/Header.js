import React from "react";
import { useNavigate, useLocation, Link } from "react-router-dom";
import styles from "./Header.module.css"; // CSS 모듈 import
import logoimg from "../img/android-chrome-192x192.png";

const Header = ({ user, onLogout }) => {
    const navigate = useNavigate();
    const location = useLocation(); // 현재 경로를 가져오기 위해 useLocation 사용

    // 로그인 버튼 클릭 시 호출되는 함수
    const handleLoginClick = () => {
        navigate("/login");
    };

    // 회원가입 버튼 클릭 시 호출되는 함수
    const handleGetStartedClick = () => {
        navigate("/join");
    };

    // 로그아웃 버튼 클릭 시 호출되는 함수
    const handleLogoutClick = () => {
        onLogout();
        navigate("/"); // 로그아웃 후 메인 페이지로 이동
    };

    const handleMypageClick = () => {
        onLogout();
        navigate("/Mypage"); // 로그아웃 후 메인 페이지로 이동
    };

    // 인증 페이지 (로그인 또는 회원가입)인지 확인
    const isAuthPage = location.pathname === "/login" || location.pathname === "/join";

    return (
        <header
            className={styles.headerContainer}
            style={{
                backgroundColor: isAuthPage ? "#f8f9fa" : "#f6f7f8", // 인증 페이지일 때 배경색 변경
            }}
        >
            <div className={styles.logo}>
                <Link to="/">
                    <img src={logoimg} alt="로고" className={styles.logoIcon} />
                </Link>
            </div>
            {!isAuthPage && ( // 인증 페이지가 아닐 때만 버튼 표시
                <div className={styles.buttonGroup}>
                    {user?.name ? (  // user 객체가 존재할 때만 확인
                        <>
                            <button className={`${styles.button} ${styles.loginnout}`} onClick={handleLogoutClick}>
                                로그아웃
                            </button>
                            <button className={`${styles.button} ${styles.loginnout}`} onClick={handleMypageClick}>
                                마이페이지
                            </button>
                        </>
                    ) : (
                        <>
                            <button className={`${styles.button} ${styles.loginnout}`} onClick={handleLoginClick}>
                                로그인
                            </button>
                            <button className={`${styles.button} ${styles.getStarted}`} onClick={handleGetStartedClick}>
                                회원가입
                            </button>
                        </>
                    )}
                </div>
            )}
        </header>
    );
};

export default Header;
