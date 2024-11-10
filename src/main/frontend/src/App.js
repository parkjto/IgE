import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Main from './pages/Main';
import Map from './pages/Map';
import Join from './pages/Join';
import MenuList from './pages/MenuList';

function App() {
    const [userData, setUserData] = useState(() => {
        const storedData = localStorage.getItem('userData');
        return storedData ? JSON.parse(storedData) : { user_ige: [], name: '' };
    });

    const handleLogout = () => {
        localStorage.removeItem('userData'); // 로컬 스토리지에서 사용자 정보 제거
        setUserData({ user_ige: [], name: '' }); // 상태를 초기화
    };

    // userData 상태를 콘솔에 출력하여 확인
    console.log("Current userData in App:", userData);

    return (
        <Router>
            <Routes>
                <Route path="/login" element={<Login setUserData={setUserData} />} />
                <Route path="/join" element={<Join />} />
                <Route path="/map" element={<Map />} />
                <Route path="/menuList" element={<MenuList userData={userData} />} /> {/* userData를 MenuList로 전달 */}
                <Route path="/" element={<Main user={userData} onLogout={handleLogout} />} /> {/* 기본 경로에 사용자 정보 전달 */}
                <Route path="*" element={<Main user={userData} onLogout={handleLogout} />} /> {/* 존재하지 않는 경로에도 사용자 정보 전달 */}
            </Routes>
        </Router>
    );
}

export default App;
