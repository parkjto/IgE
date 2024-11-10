import React from 'react';
import MenuList from "./MenuList";
import styles from './RenderLoggedIn.module.css';

const RenderLoggedIn = ({ user }) => {
    return (
        <div className={styles.container}>
            <h1 className={styles.welcomeText}>메뉴를 추천해 드릴게요 </h1>
            <p className={styles.allergyInfo}>
                {user.name} 님은 {user.allergies && user.allergies.length > 0
                ? `${user.allergies.join(', ')} 알레르기가 있으시네요!!`
                : '알레르기가 없으시네요!!'}
            </p>

            <br/>
            {/* 메뉴 리스트  */}
            <MenuList userData={user} />

        </div>
    );
};

export default RenderLoggedIn;
