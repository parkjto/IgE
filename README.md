# 🍽️ IgE - 음식 알레르기 정보 & 맞춤 메뉴 추천 서비스

IgE는 음식 알레르기 정보를 제공하고, 사용자 맞춤형 메뉴 추천 및 음식점 위치 안내 기능을 제공하는 웹 애플리케이션입니다.

---

## 📚 목차

1. [프로젝트 소개](#프로젝트-소개)
2. [기술 스택](#기술-스택)
3. [폴더 구조](#폴더-구조)
4. [설치 및 실행 방법](#설치-및-실행-방법)
5. [주요 기능](#주요-기능)
6. [API 명세](#api-명세)


---

## 📖 프로젝트 소개

- **IgE**는 음식 알레르기(특히 IgE 항체 관련) 정보를 제공하여, 알레르기 환자들이 안전하게 식사할 수 있도록 돕는 서비스입니다.
- 사용자는 자신의 알레르기 정보를 등록하고, 그에 맞는 메뉴 추천 및 음식점 위치 정보를 받을 수 있습니다.

---

## 🛠️ 기술 스택

- **백엔드:** Java 17, Spring Boot, JPA, REST API
- **프론트엔드:** React.js, JavaScript, CSS
- **DB:** (예: MySQL, H2 등)
- **지도 API:** ( Kakao Map API)
- **빌드/관리:** Gradle, npm

---

## 📁 폴더 구조

```
IgE/
├── build.gradle
├── settings.gradle
├── src/
│   ├── main/
│   │   ├── java/                # 백엔드(Spring Boot)
│   │   │   └── com/SW/IgE/
│   │   │       ├── controller/  # REST API 컨트롤러
│   │   │       ├── service/     # 비즈니스 로직
│   │   │       ├── entity/      # JPA 엔티티
│   │   │       ├── repository/  # JPA 레포지토리
│   │   │       └── ...          # 기타 설정
│   │   └── frontend/            # 프론트엔드(React)
│   │       ├── public/
│   │       └── src/
│   │           ├── pages/       # 주요 페이지 컴포넌트
│   │           └── img/         # 이미지 리소스
│   └── test/                    # 테스트 코드
└── README.md
```

---

## ⚡ 설치 및 실행 방법

### 1. 프로젝트 클론

```bash
git clone https://github.com/your-username/IgE.git
cd IgE
```

### 2. 백엔드(Spring Boot) 실행

1. `application.properties`에서 DB 설정을 확인/수정하세요.
2. Gradle 빌드 및 실행:
    ```bash
    ./gradlew bootRun
    ```
   또는 IDE에서 `IgEApplication.java` 실행

### 3. 프론트엔드(React) 실행

```bash
cd src/main/frontend
npm install
npm start
```
- 기본적으로 [http://localhost:3000](http://localhost:3000)에서 실행됩니다.

---

## 🌟 주요 기능

- **회원가입/로그인/마이페이지**: 사용자 정보 및 알레르기 정보 관리
- **음식 알레르기 정보 제공**: 다양한 음식의 알레르기 유발 성분 안내
- **맞춤형 메뉴 추천**: 사용자의 알레르기 정보 기반 안전한 메뉴 추천
- **음식점 위치 안내**: 지도 기반 음식점 위치 제공
- **랜덤 메뉴 추천**: 오늘 뭐 먹지? 고민 해결 랜덤 메뉴 추천

---

## 📑 API 명세 (예시)

| 메서드 | 엔드포인트         | 설명                |
|--------|-------------------|---------------------|
| POST   | /api/user/join    | 회원가입            |
| POST   | /api/user/login   | 로그인              |
| GET    | /api/menu/list    | 전체 메뉴 조회      |
| GET    | /api/inform/{id}  | 음식 알레르기 정보  |
| ...    | ...               | ...                 |

> 자세한 API 명세는 `/src/main/java/com/SW/IgE/controller/` 참고

---
