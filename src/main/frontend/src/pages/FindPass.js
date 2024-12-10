import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import styles from './Join.module.css';
import Header from "./Header";

function FindPass() {
    const [userInfo, setUserInfo] = useState({
        useremail: '',
        name: '',
        age: '',
        allergies: [],
        newPassword: '',
        confirmPassword: ''
    });
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { id, value } = e.target;
        setUserInfo(prevInfo => ({ ...prevInfo, [id]: value }));
        setErrorMessage('');
    };

    const handleAllergyChange = (e) => {
        const { value, checked } = e.target;
        setUserInfo(prevInfo => ({
            ...prevInfo,
            allergies: checked 
                ? [...prevInfo.allergies, value]
                : prevInfo.allergies.filter(allergy => allergy !== value)
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!userInfo.useremail || !userInfo.name || !userInfo.age || !userInfo.newPassword) {
            setErrorMessage('모든 필드를 입력해주세요.');
            return;
        }

        if (userInfo.newPassword !== userInfo.confirmPassword) {
            setErrorMessage('새 비밀번호가 일치하지 않습니다.');
            return;
        }

        try {
            const response = await axios.post('http://localhost:8081/reset-password', {
                useremail: userInfo.useremail,
                name: userInfo.name,
                age: userInfo.age,
                allergies: userInfo.allergies,
                newPassword: userInfo.newPassword
            });

            setSuccessMessage('비밀번호가 성공적으로 변경되었습니다.');
            setTimeout(() => {
                navigate('/login');
            }, 2000);
        } catch (error) {
            console.error('비밀번호 재설정 에러:', error);
            setErrorMessage(error.response?.data || '비밀번호 재설정에 실패했습니다.');
        }
    };

    return (
        <div>
            <Header />
            <div className={styles.container}>
                <h3>비밀번호 찾기</h3>
                {errorMessage && <div className={styles.errorMessage}>{errorMessage}</div>}
                {successMessage && <div className={styles.successMessage}>{successMessage}</div>}
                
                <form onSubmit={handleSubmit}>
                    <div className={styles.inputGroup}>
                        <input
                            type="email"
                            id="useremail"
                            className={styles.inputField}
                            value={userInfo.useremail}
                            placeholder="이메일"
                            onChange={handleChange}
                            required
                        />
                        <input
                            type="text"
                            id="name"
                            className={styles.inputField}
                            value={userInfo.name}
                            placeholder="이름"
                            onChange={handleChange}
                            required
                        />
                        <input
                            type="number"
                            id="age"
                            className={styles.inputField}
                            value={userInfo.age}
                            placeholder="나이"
                            onChange={handleChange}
                            required
                        />
                        <input
                            type="password"
                            id="newPassword"
                            className={styles.inputField}
                            value={userInfo.newPassword}
                            placeholder="새 비밀번호"
                            onChange={handleChange}
                            required
                        />
                        <input
                            type="password"
                            id="confirmPassword"
                            className={styles.inputField}
                            value={userInfo.confirmPassword}
                            placeholder="새 비밀번호 확인"
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className={styles.checkboxContainer}>
                        <h4 className={styles.allergyTitle}>알레르기 선택</h4>
                        <div className={styles.allergySelection}>
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
                                                        checked={userInfo.allergies.includes(allergy)}
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
                    </div>

                    <button type="submit" className={styles.button}>비밀번호 재설정</button>
                </form>
            </div>
        </div>
    );
}

export default FindPass;
