# Zero-Gap Backend API Specification (Final)

이 문서는 Zero-Gap 모바일 애플리케이션의 최종 백엔드 API 명세를 정의합니다.
모든 API는 로컬 DB 없이 서버와 데이터를 주고받으며, JWT 기반 인증을 사용합니다.

## 1. 개요 (Overview)
- **Base URL**: `https://api.zero-gap.com/v1` (Mock Server 사용 중)
- **Content-Type**: `application/json; charset=utf-8`
- **Authorization**: `Bearer <token>` (로그인 이후 모든 요청 헤더에 포함)

---

## 2. 인증 및 사용자 (Authentication & User)

### **회원가입**
`POST /auth/signup`
- **Request**:
  ```json
  {
    "email": "user@example.com",
    "password": "password123",
    "name": "홍길동"
  }
  ```
- **Response**:
  ```json
  {
    "message": "회원가입 성공",
    "token": "eyJhbGciOiJIUzI1..." 
  }
  ```

### **로그인**
`POST /auth/login`
- **Request**:
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **Response**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1...",
    "user": {
      "id": 1,
      "email": "user@example.com",
      "name": "홍길동"
    }
  }
  ```

### **프로필 조회**
`GET /user/profile`
- **Response**:
  ```json
  {
    "job": "취준생",
    "focus": "커리어 성장",
    "trait": "계획적",
    "goal": "몰입",
    "interests": ["코딩", "독서"]
  }
  ```

### **프로필 업데이트**
`POST /user/profile`
- **Request**:
  ```json
  {
    "job": "직장인",
    "focus": "번아웃 탈출",
    "trait": "즉흥적",
    "goal": "휴식",
    "interests": ["운동", "여행"]
  }
  ```
- **Response**:
  ```json
  {
    "message": "프로필 업데이트 성공",
    "token": "..." 
  }
  ```

---

## 3. 행동 챌린지 (Missions)

### **오늘의 추천 미션 조회**
`GET /missions/today`
- **Query Params**: `duration` (초 단위, 예: 600)
- **Response**:
  ```json
  {
    "missionRecommendations": [
      "스트레칭 하기",
      "물 한 잔 마시기",
      "명상 5분"
    ]
  }
  ```

### **미션 목록 조회**
`GET /missions`
- **Query Params**: `startDate` (YYYY-MM-DD), `endDate` (YYYY-MM-DD)
- **Response**:
  ```json
  {
    "missions": [
      {
        "id": 101,
        "name": "스트레칭 하기",
        "date": "2026-02-07",
        "accomplished": true,
        "description": "상쾌했다!"
      }
    ]
  }
  ```

### **미션 등록**
`POST /missions`
> **Idempotency Check**: 동일한 날짜 (`date`)에 동일한 이름(`name`)의 미션이 이미 존재하면, 새로 생성하지 않고 기존 ID를 반환합니다.
- **Request**:
  ```json
  {
    "name": "책 읽기",
    "date": "2026-02-07"
  }
  ```
- **Response**:
  ```json
  {
    "id": 102,
    "cheerMessage": "새로운 도전을 응원합니다!"
  }
  ```

### **미션 완료 처리**
`PATCH /missions/{id}`
- **Request**:
  ```json
  {
    "accomplished": true,
    "description": "집중해서 잘 읽었다."
  }
  ```
- **Response**:
  ```json
  {
    "cheerMessage": "축하합니다! 오늘도 한 걸음 성장하셨네요."
  }
  ```

---

## 4. 감정 일기 (Emotions)

### **감정 기록 조회**
`GET /emotions`
- **Query Params**: `startDate`, `endDate`
- **Response**:
  ```json
  {
    "emotions": [
      {
        "id": 501,
        "score": 5,
        "date": "2026-02-06",
        "description": "최고의 하루!"
      }
    ]
  }
  ```

### **감정 일기 작성**
`POST /emotions`
> **Upsert Logic**: 해당 날짜 (`date`)에 이미 기록된 일기가 있다면 **덮어씁니다(Update)**. 없다면 새로 생성(Insert)합니다.
- **Request**:
  ```json
  {
    "score": 3,
    "date": "2026-02-07",
    "description": "오늘은 조금 피곤했지만 보람찼다."
  }
  ```
- **Response**:
  ```json
  {
    "id": 502
  }
  ```

---

## 5. 요약 및 분석 (Summary)

### **월간 요약 조회**
`GET /summary`
- **Response**:
  ```json
  {
    "summary": "이번 달은 자기계발에 집중하셨군요! 에너지가 상승곡선을 그리고 있습니다."
  }
  ```
