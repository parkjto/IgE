import React, { useEffect, useState } from 'react';


const UserList = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await fetch('http://localhost:8081/IgE/user'); // 백엔드 API URL
                if (!response.ok) {
                    throw new Error('네트워크 응답이 올바르지 않습니다');
                }
                const data = await response.json();
                setUsers(data);
            } catch (error) {
                setError(error.message);
                console.error('사용자 정보를 가져오는 중 오류 발생:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchUsers();
    }, []);

    if (loading) {
        return <p>로딩 중...</p>;
    }

    if (error) {
        return <p>오류 발생: {error}</p>;
    }
    return (
        <div>
            {users.length > 0 ? (
                users.map((user) => (
                    <div key={user.id}>
                        <h3>{user.name} {user.age} {user.igE}</h3>
                    </div>
                ))
            ) : (
                <p>사용자가 없습니다</p>
            )}
        </div>
    );
};

export default UserList; // 올바르게 UserList 컴포넌트를 내보냅니다
