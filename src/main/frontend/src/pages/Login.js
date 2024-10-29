import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';

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

            // 로그인 성공 시 응답 값 콘솔 출력
            console.log('로그인 응답:', response.data.useremail, response.data.name, response.data.role, response.data.allergies); // 응답 데이터 콘솔 출력

            if (response.status === 200) {
                alert('로그인 성공!');
                localStorage.setItem('userData', JSON.stringify(response.data)); // 사용자 정보 저장
                setUserData({
                    useremail: response.data.useremail,
                    role: response.data.role,
                    allergies: response.data.allergies, // 알레르기 정보 추가
                    name: response.data.name
                }); // 사용자 데이터 상태 업데이트
                navigate('/'); // 기본 경로로 리디렉션
                setUser({ useremail: '', password: '' }); // 입력 필드 초기화
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
        <div>
            <Link to="/">메인 페이지</Link>
            <h3>로그인</h3>
            {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="useremail"
                    placeholder="이메일"
                    value={user.useremail}
                    onChange={handleChange}
                    required
                />
                <br />
                <input
                    type="password"
                    name="password"
                    placeholder="비밀번호"
                    value={user.password}
                    onChange={handleChange}
                    required
                />
                <button type="submit" disabled={loading}>
                    {loading ? '로딩 중...' : '로그인'}
                </button>
            </form>
            <Link to="/join">회원가입</Link>
        </div>
    );
}

export default Login;
