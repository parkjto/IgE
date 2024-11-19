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


function App() {
    const [userData, setUserData] = useState(() => {
        const storedData = localStorage.getItem('userData');
        return storedData ? JSON.parse(storedData) : { useremail: '', role: '', allergies: [], name: '' };
    });


    const handleLogout = () => {
        localStorage.removeItem('userData');
        setUserData({ useremail: '', role: '', allergies: [], name: '' });
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
                {/*<Route path="/map" element={<Map />} />*/}
                <Route path="/recipe" element={<Recipe />} />
                <Route path="/UseSearch" element={<UseSearch />} />
                <Route path="/search" element={<Search />} />
                <Route path="/MapTest" element={<MapTest />} />
                {/*<Route path="/mapc" element={<MapComponent />} />*/}
                <Route path="/rlocation" element={<RestaurantLocation />} />
                <Route path="/menuList" element={<MenuList userData={userData} />} />
                <Route path="/" element={<Main user={userData} onLogout={handleLogout} />} />
            </Routes>
        </Router>
    );
}

export default App;
