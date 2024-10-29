import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

const RenderLoggedIn = ({user, onLogout}) => {

    const navigate = useNavigate();

    const handleLogout = () => {
        onLogout(); // App에서 전달받은 로그아웃 함수 호출
        navigate('/'); // 기본 경로로 리디렉션
    };

    return (
        <div>
                <label onClick={handleLogout} style={{cursor: 'pointer', color: 'blue'}}>로그아웃</label>
                <h1>Main 페이지</h1>
                <p>
                    {user.name} 님은 {user.allergies && user.allergies.length > 0 ? user.allergies.join(', ') + ' 알레르기가 있으시네요!!' : '알레르기가 없으시네요!!'}
                </p>
                <Link to="/map">MAP</Link>
                <br/>
                <Link to="/menu">메뉴</Link>
        </div>
    );
};
export default RenderLoggedIn;