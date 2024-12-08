import React, { useState, useEffect } from "react";
import Header from "./Header";
import axios from "axios";
import styles from "./Mypage.module.css";

const Mypage = ({ user, onLogout }) => {
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

    useEffect(() => {
        if (user && user.id) {
            setIsLoggedIn(true);
            setUpdatedInfo({
                id: user.id,
                name: user.name,
                age: user.age,
                allergies: user.allergies || [],
                password: "",
                rePassword: "",
            });
        } else {
            setIsLoggedIn(false);
        }
    }, [user]);

    const handleEditClick = () => setIsEditing(true);

    const handleSave = async () => {
        if (updatedInfo.password && updatedInfo.password !== updatedInfo.rePassword) {
            alert("비밀번호가 일치하지 않습니다.");
            return;
        }

        try {
            const response = await axios.put("http://localhost:8081/update", updatedInfo, {
                headers: {
                    "Content-Type": "application/json",
                },
            });

            alert("수정된 정보가 저장되었습니다.");
            setIsEditing(false);
            console.log("수정된 사용자 정보:", response.data);
        } catch (error) {
            console.error("Error message:", error.message);
            console.error("Response data:", error.response?.data);
            console.error("Status code:", error.response?.status);
            alert("수정에 실패했습니다.");
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setUpdatedInfo((prevInfo) => ({
            ...prevInfo,
            [name]: value || "",
        }));
    };

    const handleAllergyChange = (e) => {
        const { value, checked } = e.target;
        setUpdatedInfo((prevInfo) => {
            const updatedAllergies = checked
                ? [...prevInfo.allergies, value]
                : prevInfo.allergies.filter((ige) => ige !== value);
            return { ...prevInfo, allergies: updatedAllergies };
        });
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
                                {user.allergies?.length > 0 ? user.allergies.join(", ") : "알레르기가 없으시네요."}
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

