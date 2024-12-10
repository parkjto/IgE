import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import styles from './Join.module.css'; // CSS 모듈 import
import Header from "./Header"; // Header 컴포넌트 import

function Join() {
    const [user, setUser] = useState({
        name: '',
        age: '',
        useremail: '',
        password: '',
        user_ige: [], // 알레르기 상태 초기화
    });

    const [passwordConfirm, setPasswordConfirm] = useState(''); // 비밀번호 확인 상태 추가
    const [hasAllergy, setHasAllergy] = useState(false); // 알레르기 선택 여부 상태
    const [errorMessage, setErrorMessage] = useState(''); // 에러 메시지 상태 추가
    const navigate = useNavigate(); // 페이지 이동을 위한 useNavigate 훅

    const handleChange = (e) => {
        const { id, value } = e.target;
        setUser(prevUser => ({ ...prevUser, [id]: value }));
        setErrorMessage(''); // 입력 시 에러 메시지 초기화
    };

    const handleAllergyChange = (e) => {
        const { value, checked } = e.target;
        setUser(prevState => {
            const updatedAllergies = checked
                ? [...prevState.user_ige, value] // 체크 시 알레르기 추가
                : prevState.user_ige.filter(ige => ige !== value); // 체크 해제 시 알레르기 제거
            return { ...prevState, user_ige: updatedAllergies }; // 알레르기 리스트 업데이트
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!user.name || !user.useremail || !user.password || !passwordConfirm || !user.age) {
            alert('모든 필드를 올바르게 입력해주세요.');
            return;
        }

        if (user.password !== passwordConfirm) {
            setErrorMessage('비밀번호가 일치하지 않습니다.'); // 비밀번호 불일치 에러 메시지
            return;
        }

        const formData = {
            ...user,
            age: user.age ? Number(user.age) : null, // 나이를 숫자로 변환 (입력 시에만 변환)
        };

        try {
            const response = await axios.post('http://localhost:8081/join', formData, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            alert('회원가입 완료');
            navigate('/'); // 회원가입 성공 시 메인 페이지로 이동
        } catch (error) {
            console.error('회원가입 에러: ', error.response ? error.response.data : error);
            if (error.response && error.response.status === 409) {
                setErrorMessage(error.response.data); // 이미 존재하는 이메일에 대한 에러 메시지
            } else {
                setErrorMessage('회원가입 중 오류가 발생했습니다. 다시 시도해 주세요.'); // 일반 오류 메시지
            }
        }
    };

    return (
        <div>
            <Header user={user} />

            <div className={styles.container}>
                <h3>회원가입</h3>
                {errorMessage && <div className={styles.errorMessage}>{errorMessage}</div>} {/* 에러 메시지 표시 */}
                <form onSubmit={handleSubmit}>
                    <div className={styles.inputGroup}>
                        <input
                            type="text"
                            id="name"
                            className={styles.inputField}
                            value={user.name}
                            placeholder="이름"
                            onChange={handleChange}
                            required
                        />
                        <input
                            type="number"
                            id="age"
                            className={styles.inputField}
                            value={user.age}
                            placeholder="나이"
                            onChange={handleChange}
                            required
                        />
                        <input
                            type="email"
                            id="useremail"
                            className={styles.inputField}
                            value={user.useremail}
                            placeholder="이메일"
                            onChange={handleChange}
                            required
                        />
                        <input
                            type="password"
                            id="password"
                            className={styles.inputField}
                            value={user.password}
                            placeholder="비밀번호"
                            onChange={handleChange}
                            required
                        />
                        <input
                            type="password"
                            id="passwordConfirm"
                            className={styles.inputField}
                            value={passwordConfirm}
                            placeholder="비밀번호 확인"
                            onChange={(e) => setPasswordConfirm(e.target.value)}
                            required
                        />
                    </div>

                    <div className={styles.checkboxContainer}>
                        <label className={styles.checkboxLabel}>
                            <input
                                type="checkbox"
                                className={styles.checkboxInput}
                                checked={hasAllergy}
                                onChange={(e) => setHasAllergy(e.target.checked)}
                            /> 알레르기
                        </label>

                        {hasAllergy && (
                            <div className={styles.allergySelection}>
                                <h4 className={styles.allergyTitle}>알레르기 선택</h4>
                                <table className={styles.allergyTable}>
                                    <tbody>
                                    {[
                                        ["계란", "콩"],
                                        ["우유", "밀"],
                                        ["땅콩", "생선"],
                                        ["갑각류", "견과류"]
                                    ].map((row, rowIndex) => (
                                        <tr key={rowIndex}>
                                            {row.map((allergy) => (
                                                <td key={allergy} className={styles.allergyCell}>
                                                    <label className={styles.allergyOption}>
                                                        <input
                                                            className={styles.allergyCheckbox}
                                                            type="checkbox"
                                                            value={allergy}
                                                            checked={user.user_ige.includes(allergy)}
                                                            onChange={handleAllergyChange}
                                                        />
                                                        {allergy}
                                                    </label>
                                                </td>
                                            ))}
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </div>

                    <button type="submit" className={styles.button}>회원가입</button>
                </form>
            </div>
        </div>
    );
}

export default Join;
