import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

const Main = ({ user, onLogout }) => {
    const navigate = useNavigate();

    const handleLogout = () => {
        onLogout(); // App에서 전달받은 로그아웃 함수 호출
        navigate('/'); // 기본 경로로 리디렉션
    };

    const renderLoggedInUser = () => (
        <div>
            <label onClick={handleLogout} style={{ cursor: 'pointer', color: 'blue' }}>로그아웃</label>
            <h1>Main 페이지</h1>
            <p>
                {user.name} 님은 {user.allergies && user.allergies .length > 0 ? user.allergies.join(', ') + ' 알레르기가 있으시네요!!' : '알레르기가 없으시네요!!'}
            </p>
            <Link to="/map">MAP</Link>
            <br />
            <Link to="/menu">메뉴</Link>
        </div>
    );

    const renderLoggedOutUser = () => (
        <div>
            <Link to="/login" style={{color: 'blue', textDecoration: "none"}}>로그인</Link> <Link to="/join" style={{color: 'blue', textDecoration: "none"}}>회원가입</Link>
            <h1>Main 페이지</h1>
            <p>로그인 후 메뉴를 추천 받으세요!.</p>
        </div>
    );

    return (
        <div style={{ padding: '20px' }}>
            {user.name ? renderLoggedInUser() : renderLoggedOutUser()}
        </div>
    );
};

export default Main;
