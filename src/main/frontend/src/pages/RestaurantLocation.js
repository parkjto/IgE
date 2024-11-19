import React from 'react';
import Header from "./Header";
import MapComponent from "./MapComponent";
import Search from "./Search";


const RestaurantLocation = ({ user, onLogout }) => {
    return (
        <div>
            <Header user={user} onLogout={onLogout} />

            {/*<Map />*/}
            <br/>
            <Search/>
        </div>
    );
};

export default RestaurantLocation;
