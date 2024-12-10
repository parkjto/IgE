import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Main from './pages/Main';
import Join from './pages/Join';
import MenuList from './pages/MenuList';
import Recipe from "./pages/Recipe";
import RestaurantLocation from "./pages/RestaurantLocation";
import Search from "./pages/Search";
import MapTest from "./pages/KakaoMapTest";
import UseSearch from "./pages/useSearch";
import Mypage from "./pages/Mypage";

import FindPass from "./pages/FindPass";

function App() {
    const [userData, setUserData] = useState(() => {
        const storedData = localStorage.getItem('userData');
        return storedData ? JSON.parse(storedData) : { 
            id: '',
            useremail: '', 
            role: '', 
            allergies: [], 
            name: '', 
            age: '',
            user_ige: [] // user_ige 필드 추가
        };
    });

    const handleLogout = () => {
        localStorage.removeItem('userData');
        setUserData({ id : '', useremail: '', role: '', allergies: [], name: '', age: '' });
    };

    // 사용자 정보 업데이트 함수 수정
    const handleUserUpdate = (updatedUserInfo) => {
        const newUserData = {
            ...userData,
            ...updatedUserInfo,
            allergies: updatedUserInfo.user_ige || updatedUserInfo.allergies || [] // 둘 다 확인
        };
        setUserData(newUserData);
        localStorage.setItem('userData', JSON.stringify(newUserData));
    };

    useEffect(() => {
        const storedData = localStorage.getItem('userData');
        if (storedData) {
            setUserData(JSON.parse(storedData));
        }
    }, []);

    // userData가 변경될 때마다 localStorage에 저장
    useEffect(() => {
        if (userData.useremail) {
            localStorage.setItem('userData', JSON.stringify(userData));
        }
    }, [userData]);

    return (
        <Router>
            <Routes>
                <Route path="/login" element={<Login setUserData={setUserData} />} />
                <Route path="/join" element={<Join />} />
                <Route path="/findPass" element={<FindPass />} />
                <Route path="/recipe" element={<Recipe user={userData} setUserData={setUserData} onLogout={handleLogout}/>} />
                <Route path="/UseSearch" element={<UseSearch />} />
                <Route path="/search" element={<Search user={userData} onLogout={handleLogout}/>} />
                <Route path="/MapTest" element={<MapTest user={userData} onLogout={handleLogout}/>} />
                <Route path='/Mypage' element={<Mypage user={userData} onLogout={handleLogout} onUserUpdate={handleUserUpdate}/>} />
                
                <Route path="/rlocation" element={<RestaurantLocation user={userData} onLogout={handleLogout}/>} />
                <Route path="/menuList" element={<MenuList userData={userData} />} />
                <Route path="/" element={<Main user={userData} onLogout={handleLogout} />} />
            </Routes>
        </Router>
    );
}

export default App;
