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

function App() {
    const [userData, setUserData] = useState(() => {
        const storedData = localStorage.getItem('userData');
        return storedData ? JSON.parse(storedData) : { useremail: '', role: '', allergies: [], name: '' , age: ''};
    });


    const handleLogout = () => {
        localStorage.removeItem('userData');
        setUserData({ useremail: '', role: '', allergies: [], name: '', age: '' });
    };

    useEffect(() => {
        const storedData = localStorage.getItem('userData');
        if (storedData) {
            setUserData(JSON.parse(storedData));
        }
    }, []);

    return (
        <Router>
            <Routes>
                <Route path="/login" element={<Login setUserData={setUserData} />} />
                <Route path="/join" element={<Join />} />
                <Route path="/recipe" element={<Recipe user={userData} setUserData={setUserData} onLogout={handleLogout}/>} />
                <Route path="/UseSearch" element={<UseSearch />} />
                <Route path="/search" element={<Search user={userData}  onLogout={handleLogout}/> } />
                <Route path="/MapTest" element={<MapTest user={userData} onLogout={handleLogout}/>} />
                <Route path='/Mypage' element={<Mypage user={userData} setUserData={setUserData}  onLogout={handleLogout} />} />
                <Route path="/rlocation" element={<RestaurantLocation user={userData} setUserData={setUserData} onLogout={handleLogout}/>} />
                <Route path="/menuList" element={<MenuList userData={userData} />} />
                <Route path="/" element={<Main user={userData} onLogout={handleLogout} />} />
            </Routes>
        </Router>
    );
}

export default App;
