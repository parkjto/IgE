import React from 'react';
// import { useNavigate } from 'react-router-dom';  // useNavigate import
import RandomMenu from "./RandomMenu";
import InformIgE from "./InformIgE";
import styles from './RenderLoggedIn.module.css';

const RenderLoggedIn = ({ user, isLoggedIn }) => {


    return (
        <div className={styles.Informcontainer}>
            <InformIgE isLoggedIn={isLoggedIn}/> {/* isLoggedIn을 InformIgE로 전달 */}
            <div className={styles.container}>
                <p className={styles.welcomeText}>오늘은 뭐를 먹어볼까나?? </p>
                <p className={styles.allergyInfo}>
                    {user?.name} 님은 {user?.allergies && user.allergies.length > 0
                    ? `${user.allergies.join(', ')} 알레르기가 있으시네요!!`
                    : '알레르기가 없으시네요!!'}
                </p>

                <br/>

                <RandomMenu userData={user}/>
            </div>

        </div>
    );
};

export default RenderLoggedIn;
