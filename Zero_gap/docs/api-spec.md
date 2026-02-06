# Zero-Gap API Specification (Revised)

본 문서는 Zero-Gap 모바일 앱과 백엔드 간의 통신을 위한 API 규격서입니다. 
로컬 DB를 사용하지 않고 모든 데이터를 백엔드 API를 통해 관리합니다.

## 1. 개요
- **Base URL**: `https://api.zero-gap.com/` (테스트용)
- **인증**: JWT (Login 후 발급받은 Token을 Authorization Header에 `Bearer {token}` 형식으로 포함)
- **데이터 형식**: JSON

## 2. API 상세

### 2.0 인증 및 사용자 프로필 (Auth & User Profile)

| 메소드 | URL | 설명 | 요청 바디 | 응답 |
| --- | --- | --- | --- | --- |
| POST | `/auth/signup` | 회원가입 | `{ "email": String, "password": String, "name": String }` | `{ "message": String, "token": String }` |
| POST | `/auth/login` | 로그인 | `{ "email": String, "password": String }` | `{ "token": String, "user": { "id": Int, "email": String, "name": String } }` |
| GET | `/user/profile` | 사용자 프로필 조회 | - | `{ "job": String, "focus": String, "trait": String, "goal": String, "interests": [String] }` |
| POST | `/user/profile` | 프로필 정보 저장/업데이트 | `{ "job": String, "focus": String, "trait": String, "goal": String, "interests": [String] }` | `{ "message": String }` |

### 2.1 행동 챌린지 (Mission)

| 메소드 | URL | 설명 | 쿼리 파라미터 | 요청 바디 | 응답 |
| --- | --- | --- | --- | --- | --- |
| GET | `/missions` | 기간 내 미션 조회 | `startDate`, `endDate` | - | `{ "missions": [ { "id": Int, "name": String, "date": String, "accomplished": Boolean, "description": String } ] }` |
| GET | `/missions/today`| 오늘의 권장 미션 요청 | `durationInSeconds` | - | `{ "missionRecommendations": [ "책 한 권 읽기", "운동하기", ... ] }` |
| POST | `/missions` | 미션 등록 (수행 전) | - | `{ "name": String, "date": String }` | `{ "id": Int, "cheerMessage": String }` |
| PATCH | `/missions/{id}` | 미션 완수 (상태 변경) | - | `{ "accomplished": Boolean, "description": String }` | `{ "cheerMessage": String }` |

### 2.2 감정 일기 (Emotion)

| 메소드 | URL | 설명 | 쿼리 파라미터 | 요청 바디 | 응답 |
| --- | --- | --- | --- | --- | --- |
| GET | `/emotions` | 기간 내 감정 기록 조회 | `startDate`, `endDate` | - | `{ "emotions": [ { "id": Int, "score": Int, "date": String, "description": String } ] }` |
| POST | `/emotions` | 오늘의 감정 등록 | - | `{ "score": Int, "date": String, "description": String }` | `{ "id": Int }` |

### 2.3 AI 요약 (Summary)

| 메소드 | URL | 설명 | 쿼리 파라미터 | 요청 바디 | 응답 |
| --- | --- | --- | --- | --- | --- |
| GET | `/summary` | 기간 내 데이터 AI 한줄평 | `startDate`, `endDate` | - | `{ "summary": String }` |

## 3. 데이터 타입 (Data Types)

### Mission
- `id`: 고유 식별자 (Long/Int)
- `name`: 미션 내용 (String)
- `date`: 발생 날짜 (String, YYYY-MM-DD)
- `accomplished`: 성공 여부 (Boolean)
- `description`: 사용자의 소감 (String)

### Emotion
- `id`: 고유 식별자 (Long/Int)
- `score`: 감정 점수 (Int, 1~5)
- `date`: 발생 날짜 (String, YYYY-MM-DD)
- `description`: 일기 내용 (String)