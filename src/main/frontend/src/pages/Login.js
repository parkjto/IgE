import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import styles from './Login.module.css';
import logoimg from "../img/android-chrome-192x192.png";

const API_URL = 'http://localhost:8081/login';

function Login({ setUserData }) {
    const navigate = useNavigate();
    const [user, setUser] = useState({ useremail: '', password: '' });
    const [loading, setLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUser((prevState) => ({ ...prevState, [name]: value }));
    };

    const validateEmail = (email) => {
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailPattern.test(email);
    };

    const getCsrfToken = () => {
        return document.cookie
            .split('; ')
            .find(row => row.startsWith('XSRF-TOKEN='))?.split('=')[1];
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrorMessage('');

        // 입력 값 유효성 검사
        if (!user.useremail || !user.password) {
            setErrorMessage('이메일과 비밀번호를 입력해주세요.');
            return;
        }

        if (!validateEmail(user.useremail)) {
            setErrorMessage('유효한 이메일 주소를 입력해주세요.');
            return;
        }

        setLoading(true);
        try {
            const response = await axios.post(API_URL, {
                useremail: user.useremail,
                password: user.password
            }, {
                headers: { 'X-XSRF-TOKEN': getCsrfToken() }
            });

            if (response.status === 200) {
                // 로그인 성공 후 데이터 저장
                alert('로그인 성공!');
                localStorage.setItem('userData', JSON.stringify(response.data));
                setUserData({
                    id : response.data.id,
                    useremail: response.data.useremail,
                    role: response.data.role,
                    allergies: response.data.allergies,
                    name: response.data.name,
                    age : response.data.age
                });
                navigate('/'); // 로그인 성공 시 메인 페이지로 이동
            }
        } catch (error) {
            if (error.response) {
                setErrorMessage(error.response.data.message || '로그인에 실패했습니다.');
            } else {
                setErrorMessage('서버에 요청을 보내는 중 오류가 발생했습니다.');
            }
            console.error('로그인 에러: ', error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.appContainer}>
            <div className={styles.container}>
                <Link to="/">
                    <img src={logoimg} alt="로고" className={styles.logoIcon} />
                </Link>
                {errorMessage && <p className={styles.errorMessage}>{errorMessage}</p>}
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        name="useremail"
                        placeholder="이메일"
                        className={styles.inputField}
                        value={user.useremail}
                        onChange={handleChange}
                        required
                    />
                    <input
                        type="password"
                        name="password"
                        placeholder="비밀번호"
                        className={styles.inputField}
                        value={user.password}
                        onChange={handleChange}
                        required
                    />
                    <button type="submit" className={styles.button} disabled={loading}>
                        {loading ? '로딩 중...' : '로그인'}
                    </button>
                </form>
                <div className={styles.footerLinks}>
                    <Link to="/join">회원가입</Link>
                </div>
            </div>
        </div>
    );
}

export default Login;
