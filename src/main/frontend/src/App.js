import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Menu from './pages/Menu';
import Main from './pages/Main';
import Map from './pages/Map';
import Join from './pages/Join';

function App() {
    const [userData, setUserData] = useState(() => {
        const storedData = localStorage.getItem('userData');
        return storedData ? JSON.parse(storedData) : { user_ige: [], name: '' };
    });

    const handleLogout = () => {
        localStorage.removeItem('userData'); // 로컬 스토리지에서 사용자 정보 제거
        setUserData({ user_ige: [], name: '' }); // 상태를 초기화
    };

    return (
        <Router>
            <Routes>
                <Route path="/login" element={<Login setUserData={setUserData} />} />
                <Route path="/join" element={<Join />} />
                <Route path="/menu" element={<Menu />} />
                <Route path="/map" element={<Map />} />
                <Route path="/" element={<Main user={userData} onLogout={handleLogout} />} /> {/* 기본 경로에 사용자 정보 전달 */}
                <Route path="*" element={<Main user={userData} onLogout={handleLogout} />} /> {/* 존재하지 않는 경로에도 사용자 정보 전달 */}
            </Routes>
        </Router>
    );
}

export default App;
