import React, { useState, useEffect } from 'react';
import RenderLoggedIn from "./RenderLoggedIn";
import RenderLoggedOut from "./RenderLoggedOut";
import Header from "./Header";

const Main = ({ user, onLogout }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        // user.name이 존재하면 로그인 상태로 설정
        setIsLoggedIn(!!user.name);
    }, [user.name]);

    return (
        <div>
            <Header user={user} onLogout={onLogout} />
            <div>
                {isLoggedIn ? (
                    <RenderLoggedIn user={user} isLoggedIn={isLoggedIn} />
                ) : (
                    <RenderLoggedOut />
                )}
            </div>
        </div>
    );
};

export default Main;
