import React from 'react';
import Header from "./Header"; // Header 컴포넌트 import

const Recipe = ({ user, onLogout }) => {
    return (
        <div>
            <Header user={user} onLogout={onLogout} />  {/* 헤더 추가 */}
            <h1> 레시피 페이지</h1>
        </div>
    );
};

export default Recipe;
