import React, { useState, useEffect } from 'react';
import RenderLoggedIn from "./RenderLoggedIn";
import RenderLoggedOut from "./RenderLoggedOut";
import Header from "./Header";

const Main = ({ user, onLogout }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        console.log("Main 컴포넌트에서 전달받은 user 데이터:", user);
        if (user && user.name) {
            setIsLoggedIn(true);
        } else {
            setIsLoggedIn(false);
        }
    }, [user]);

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
