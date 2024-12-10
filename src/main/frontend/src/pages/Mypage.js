import React, { useState, useEffect } from "react";
import Header from "./Header";
import axios from "axios";  
import styles from "./Mypage.module.css";

const Mypage = ({ user, onLogout, onUserUpdate }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [updatedInfo, setUpdatedInfo] = useState({
        id: "",
        name: "",
        age: "",
        allergies: [],
        password: "",
        rePassword: "",
    });

    // user 정보가 변경될 때마다 updatedInfo 업데이트
    useEffect(() => {
        if (user && user.id) {
            console.log("현재 사용자 정보:", user);
            setIsLoggedIn(true);
            setUpdatedInfo({
                id: user.id,
                name: user.name,
                age: user.age,
                allergies: user.user_ige || user.allergies || [],
                password: "",
                rePassword: "",
            });
        } else {
            setIsLoggedIn(false);
        }
    }, [user]);

    const handleEditClick = () => setIsEditing(true);

    const handleSave = () => {
        // 비밀번호 유효성 검사
        if (updatedInfo.password || updatedInfo.rePassword) {
            if (updatedInfo.password !== updatedInfo.rePassword) {
                console.error("[Validation] 비밀번호 불일치");
                alert("비밀번호가 일치하지 않습니다.");
                return;
            }
            if (updatedInfo.password.length < 2) {
                console.error("[Validation] 비밀번호 길이 부족");
                alert("비밀번호는 최소 2자 이상이어야 합니다.");
                return;
            }
        }

        // 데이터 가공 및 유효성 검사
        const requestData = {
            id: updatedInfo.id,
            name: updatedInfo.name,
            age: typeof updatedInfo.age === 'string' ? parseInt(updatedInfo.age, 10) : updatedInfo.age,
            allergies: updatedInfo.allergies || [],
            password: updatedInfo.password || null
        };

        console.log("[Request] 전송할 데이터:", {
            ...requestData,
            password: requestData.password ? '***' : null  // 비밀번호는 로그에 표시하지 않음
        });

        const config = {
            method: 'POST',
            url: 'http://localhost:8081/update',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            data: requestData
        };

        console.log("[Request] 요청 설정:", {
            method: config.method,
            url: config.url,
            headers: config.headers
        });

        axios(config)
            .then(response => {
                console.log("[Response] 전체 응답:", {
                    status: response.status,
                    statusText: response.statusText,
                    headers: response.headers,
                    data: response.data
                });
                
                const updatedUserInfo = {
                    ...user,
                    name: response.data.name,
                    age: response.data.age,
                    allergies: response.data.allergies || [],
                    user_ige: response.data.user_ige || []
                };

                console.log("[Update] 업데이트된 사용자 정보:", updatedUserInfo);
                onUserUpdate(updatedUserInfo);
                alert("수정된 정보가 저장되었습니다.");
                setIsEditing(false);

                if (updatedInfo.password) {
                    alert("비밀번호가 변경되었습니다. 다시 로그인해주세요.");
                    onLogout();
                }
            })
            .catch(error => {
                console.error("[Error] 전체 에러 객체:", error);
                console.error("[Error] 요청 설정:", {
                    method: error.config?.method,
                    url: error.config?.url,
                    headers: error.config?.headers,
                    data: error.config?.data ? {
                        ...error.config.data,
                        password: error.config.data.password ? '***' : null
                    } : null
                });
                
                if (error.response) {
                    console.error("[Error] 응답 상태:", error.response.status);
                    console.error("[Error] 응답 데이터:", error.response.data);
                    console.error("[Error] 응답 헤더:", error.response.headers);
                    alert(`수정에 실패했습니다: ${error.response.data || '서버 오류가 발생했습니다.'}`);
                } else if (error.request) {
                    console.error("[Error] 요청 객체:", error.request);
                    alert("서버에 연결할 수 없습니다. 네트워크 연결을 확인해주세요.");
                } else {
                    console.error("[Error] 에러 메시지:", error.message);
                    alert("수정 중 오류가 발생했습니다: " + error.message);
                }
            });
    };

    // handleChange 함수 추가
    const handleChange = (e) => {
        const { name, value } = e.target;
        setUpdatedInfo(prevInfo => ({
            ...prevInfo,
            [name]: name === 'age' ? (value === '' ? '' : parseInt(value)) : value
        }));
    };

    // 알레르기 체크박스 처리 함수
    const handleAllergyChange = (e) => {
        const { value, checked } = e.target;
        setUpdatedInfo(prevInfo => ({
            ...prevInfo,
            allergies: checked 
                ? [...prevInfo.allergies, value]
                : prevInfo.allergies.filter(allergy => allergy !== value)
        }));
    };

    if (!isLoggedIn) {
        return (
            <div className={styles.container}>
                <Header user={null} onLogout={onLogout} />
                <div className={styles.messageBox}>
                    <p>로그인 상태가 아닙니다. 다시 로그인해주세요.</p>
                </div>
            </div>
        );
    }

    return (
        <div className={styles.container}>
            <Header user={user} onLogout={onLogout} />
            <div className={styles.content}>
                <h1 className={styles.title}>마이페이지</h1>
                <div className={styles.infoCard}>
                    <div className={styles.inputGroup}>
                        <label className={styles.label}>이름:</label>
                        {isEditing ? (
                            <input
                                className={styles.inputField}
                                name="name"
                                value={updatedInfo.name}
                                onChange={handleChange}
                            />
                        ) : (
                            <span className={styles.info}>{user.name}</span>
                        )}
                    </div>

                    <div className={styles.inputGroup}>
                        <label className={styles.label}>나이:</label>
                        {isEditing ? (
                            <input
                                className={styles.inputField}
                                name="age"
                                type="number"
                                value={updatedInfo.age}
                                onChange={handleChange}
                            />
                        ) : (
                            <span className={styles.info}>{user.age}</span>
                        )}
                    </div>

                    <div className={styles.inputGroup}>
                        <label className={styles.label}>알레르기:</label>
                        {isEditing ? (
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
                                                            checked={updatedInfo.allergies.includes(allergy)}
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
                        ) : (
                            <span className={styles.info}>
                                {user?.allergies && user.allergies.length > 0
                                    ? `${user.allergies.join(', ')}`
                                    : '알레르기가 없으시네요.'}
                            </span>
                        )}
                    </div>

                    {isEditing && (
                        <div className={styles.inputGroup}>
                            <label className={styles.label}>비밀번호:</label>
                            <input
                                className={styles.inputField}
                                name="password"
                                type="password"
                                value={updatedInfo.password}
                                onChange={handleChange}
                                placeholder="새 비밀번호"
                            />
                            <input
                                className={styles.inputField}
                                name="rePassword"
                                type="password"
                                value={updatedInfo.rePassword}
                                onChange={handleChange}
                                placeholder="새 비밀번호 확인"
                            />
                        </div>
                    )}

                    <div className={styles.buttonGroup}>
                        {isEditing ? (
                            <>
                                <button className={`${styles.button} ${styles.saveButton}`} onClick={handleSave}>
                                    저장
                                </button>
                                <button
                                    className={`${styles.button} ${styles.cancelButton}`}
                                    onClick={() => setIsEditing(false)}
                                >
                                    취소
                                </button>
                            </>
                        ) : (
                            <button className={`${styles.button} ${styles.editButton}`} onClick={handleEditClick}>
                                수정
                            </button>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Mypage;

