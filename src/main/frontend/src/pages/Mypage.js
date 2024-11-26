// import React, { useState, useEffect } from "react";
// import axios from "axios";
// import styles from "./Mypage.module.css";
// import Header from "./Header";
//
// const Mypage = () => {
//     const [userInfo, setUserInfo] = useState(null); // 초기값 null
//     const [isEditing, setIsEditing] = useState(false);
//     const [updatedInfo, setUpdatedInfo] = useState({}); // 초기값을 빈 객체로 설정
//
//     useEffect(() => {
//         const fetchUserInfo = async () => {
//             try {
//                 const email = localStorage.getItem("userEmail"); // 로컬 스토리지에서 이메일 가져오기
//                 console.log("로컬 스토리지에서 가져온 이메일:", email); // 로그 추가
//
//                 if (!email) {
//                     alert("로그인된 사용자 정보가 없습니다.");
//                     return;
//                 }
//
//                 const response = await axios.get(
//                     `http://localhost:8081/api/userInfo/${email}`, // 이메일을 이용해 사용자 정보 요청
//                     { withCredentials: true }
//                 );
//                 console.log("서버 응답:", response.data); // 서버 응답 로그 추가
//                 setUserInfo(response.data);
//                 setUpdatedInfo(response.data);
//             } catch (error) {
//                 console.error("사용자 정보 불러오기 실패:", error);
//                 alert("사용자 정보를 불러오는 데 실패했습니다.");
//             }
//         };
//
//         fetchUserInfo();
//     }, []);
//
//
//     // 편집 모드 전환
//     const handleEditToggle = () => setIsEditing(!isEditing);
//
//     // 값 변경 처리
//     const handleChange = (e) => {
//         const { name, value } = e.target;
//         setUpdatedInfo({ ...updatedInfo, [name]: value });
//     };
//
//     // 알레르기 변경 처리
//     const handleAllergyChange = (e) => {
//         const { value, checked } = e.target;
//         const updatedAllergies = checked
//             ? [...updatedInfo.user_ige, value]
//             : updatedInfo.user_ige.filter((ige) => ige !== value);
//         setUpdatedInfo({ ...updatedInfo, user_ige: updatedAllergies });
//     };
//
//     // 수정된 정보 저장
//     const handleSave = async () => {
//         try {
//             console.log("저장할 정보:", updatedInfo); // 저장될 데이터 확인
//             await axios.put(
//                 "http://localhost:8081/api/updateUser", // 수정된 정보 서버로 전송
//                 updatedInfo,
//                 { withCredentials: true }
//             );
//             alert("사용자 정보가 수정되었습니다.");
//             setUserInfo(updatedInfo); // 수정된 정보 반영
//             setIsEditing(false);
//         } catch (error) {
//             console.error("사용자 정보 수정 실패:", error);
//             alert("수정 중 문제가 발생했습니다.");
//         }
//     };
//
//     if (!userInfo) {
//         return <div>로딩 중...</div>; // 로딩 처리
//     }
//
//     return (
//         <div className={styles.container}>
//             <Header />
//             <h2 className={styles.title}>마이페이지</h2>
//
//             <div className={styles.inputGroup}>
//                 <label>이름:</label>
//                 {isEditing ? (
//                     <input
//                         name="name"
//                         className={styles.inputField}
//                         value={updatedInfo.name || ""}
//                         onChange={handleChange}
//                     />
//                 ) : (
//                     <span>{userInfo.name}</span>
//                 )}
//             </div>
//
//             <div className={styles.inputGroup}>
//                 <label>이메일:</label>
//                 <span>{userInfo.useremail}</span>
//             </div>
//
//             <div className={styles.inputGroup}>
//                 <label>나이:</label>
//                 {isEditing ? (
//                     <input
//                         name="age"
//                         type="number"
//                         className={styles.inputField}
//                         value={updatedInfo.age || ""}
//                         onChange={handleChange}
//                     />
//                 ) : (
//                     <span>{userInfo.age}</span>
//                 )}
//             </div>
//
//             <div className={styles.inputGroup}>
//                 <label>알레르기:</label>
//                 {isEditing ? (
//                     <div className={styles.allergySelection}>
//                         {["계란", "콩", "우유", "밀", "땅콩", "생선", "갑각류", "견과류"].map((ige) => (
//                             <label key={ige} className={styles.allergyOption}>
//                                 <input
//                                     type="checkbox"
//                                     value={ige}
//                                     checked={updatedInfo.user_ige.includes(ige)}
//                                     onChange={handleAllergyChange}
//                                 />
//                                 {ige}
//                             </label>
//                         ))}
//                     </div>
//                 ) : (
//                     <span>{userInfo.user_ige.join(", ")}</span>
//                 )}
//             </div>
//
//             <div className={styles.buttonGroup}>
//                 <button className={styles.button} onClick={isEditing ? handleSave : handleEditToggle}>
//                     {isEditing ? "저장" : "수정"}
//                 </button>
//             </div>
//         </div>
//     );
// };
//
// export default Mypage;
import React, { useEffect } from "react";
import Header from "./Header";

const Mypage = ({ user, onLogout }) => {
    useEffect(() => {
        console.log("Mypage에 접속했습니다."); // Mypage 접속 시 로그 출력
        console.log("현재 사용자 정보:", user); // user 정보 로그 출력
    }, [user]);

    const handleLogoutClick = () => {
        console.log("Mypage에서 로그아웃 클릭됨");
        onLogout();
    };

    return (
        <div>
            <Header/>
            <h1>마이페이지</h1>
            {user?.name ? (
                <div>
                    <p>이름: {user.name}</p>
                    <p>나이: {user.age}</p>
                    <p>이메일: {user.useremail}</p>
                    <p>알레르기: {user.allergies.join(", ")}</p>
                </div>
            ) : (
                <p>사용자 정보가 없습니다.</p>
            )}
        </div>
    );
};

export default Mypage;
