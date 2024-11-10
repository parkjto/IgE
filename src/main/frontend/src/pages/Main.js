import React from 'react';
import RenderLoggedIn from "./RenderLoggedIn";
import RenderLoggedOut from "./RenderLoggedOut";
import Header from "./Header";

const Main = ({ user, onLogout }) => {
    return (
        <div>
            <Header user={user} onLogout={onLogout} />
            <div>

                {user.name ? (
                    <RenderLoggedIn user={user} onLogout={onLogout} />
                ) : (
                    <RenderLoggedOut />
                )}
            </div>
        </div>
    );
};

export default Main;
