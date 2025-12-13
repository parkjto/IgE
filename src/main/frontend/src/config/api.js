// API 설정 파일
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8081';

export const API_ENDPOINTS = {
    LOGIN: `${API_BASE_URL}/login`,
    JOIN: `${API_BASE_URL}/join`,
    UPDATE: `${API_BASE_URL}/update`,
    RESET_PASSWORD: `${API_BASE_URL}/reset-password`,
    USER_INFO: `${API_BASE_URL}/userInfo`,
    MENUS: `${API_BASE_URL}/api/menus`,
    MENUS_RANDOM: `${API_BASE_URL}/api/menus/random`,
    MENUS_RANDOM_BY_CATEGORY: (category) => `${API_BASE_URL}/api/menus/random/${category}`,
    SEARCH_NAVER: (query) => `${API_BASE_URL}/api/search/naver?query=${encodeURIComponent(query)}`,
    ALL_INFORMS: `${API_BASE_URL}/api/all-informs`,
};

export default API_BASE_URL;

