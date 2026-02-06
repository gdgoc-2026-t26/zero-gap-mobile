# Zero-Gap: 생산적인 공백을 위한 모바일 앱 개발
## 최종 발표 자료 (Draft)

---

# 1. 프로젝트 개요 (Overview)

### **Zero-Gap이란?**
> **"당신의 공백을 낭비가 아닌, 생산적인 에너지로 채우다."**

바쁜 일상 속 짧게 발생하는 틈새 시간(Gap)을 무의미하게 보내지 않고, 사용자의 감정과 상태에 맞춘 맞춤형 **Zero-Gap Action**을 제안하여 멘탈 에너지를 회복하고 생산성을 유지하도록 돕는 모바일 애플리케이션입니다.

### **핵심 가치**
- **Personalized**: 사용자 프로필(직업, 고민, 성향) 분석 기반 맞춤 제안
- **Action-Oriented**: 생각만 하는 것이 아니라 즉시 실행 가능한 타이머 기반 챌린지
- **Emotion-Aware**: 감정 상태를 기록하고 이를 반영한 힐링/성장 미션 추천

---

# 2. 주요 기능 (Key Features)

### **A. 홈 화면 (Home Dashboard)**
- **AI 맞춤 추천**: 현재 시간과 사용자 상태에 최적화된 미션 3가지 제안 (AI Logic)
- **오늘의 현황**: 오늘 완료한 챌린지 개수, 평균 감정 점수, 성공 여부 실시간 시각화
- **감정 그래프**: 주간 감정 변화 흐름을 직관적인 막대 그래프로 제공

### **B. 행동 챌린지 (Action Challenge)**
- **시간 선택**: 5분, 10분, 30분, 1시간 등 가용 시간에 맞춘 유연한 설정
- **카운트다운 타이머**: 몰입을 돕는 심플한 원형 타이머 UI
- **멘탈 에너지 바**: 챌린지 수행 전후의 에너지 변화 시각화 및 응원 메시지

### **C. 감정 일기 (Emotion Diary)**
- **간편 기록**: 복잡한 글쓰기 대신 5단계 감정 이모지와 짧은 코멘트로 기록
- **Upsert 로직**: 하루에 여러 번 기록해도 최신 내용으로 자동 갱신
- **피드백**: 일기 저장 시 위로/응원 메시지 즉시 제공

### **D. 마이 페이지 & 프로필 설정 (Profile & Auth)**
- **원스톱 회원가입**: 계정 생성과 동시에 직업, 성향, 관심사 등 AI 분석 데이터 수집
- **JWT 인증**: 안전한 토큰 기반 로그인 및 자동 로그인 유지
- **데이터 동기화**: 모든 정보는 실시간으로 클라우드(Mock Server)와 동기화

---

# 3. 기술 아키텍처 (Technical Architecture)

### **Android Client**
- **Architecture**: MVVM (Model-View-ViewModel) 패턴 적용으로 로직과 UI 분리
- **UI Framework**: XML Layouts + ViewBinding + Material Design Components 
- **Navigation**: Jetpack Navigation Component (Single Activity Architecture)
- **Async Processing**: Kotlin Coroutines & Flow

### **Network Layer**
- **Library**: Retrofit2 & OkHttp3
- **Design**: 
    - 로컬 DB(Room) 의존성 완전 제거 (Pure API Integration)
    - 표준화된 DTO (Data Transfer Object) 설계
- **Mock Service**: 개발 단계에서의 빠른 테스팅을 위한 `MockApiService` 구현 (시나리오 테스트 지원)

---

# 4. API 명세 (API Specification)

### **Authentication & User**
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/auth/signup` | 회원가입 및 토큰 발급 |
| `POST` | `/auth/login` | 이메일 로그인 |
| `GET/POST` | `/user/profile` | AI 맞춤형 프로필 조회 및 수정 |

### **Core Data**
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/missions/today` | AI 미션 추천 (시간/상태 기반) |
| `POST` | `/missions` | 챌린지 등록 (중복 방지 로직 적용) |
| `POST` | `/emotions` | 감정 일기 작성 (Upsert 적용) |
| `GET` | `/summary` | 월간 데이터 요약 분석 |

---

# 5. 개발 성과 및 향후 계획

### **개발 성과**
- [x] 전체 UI/UX 테마(Deep Indigo) 및 디자인 시스템 구축
- [x] 핵심 기능(타이머, 일기, 달력) 완벽 구현
- [x] 데이터 동기화 이슈 및 내비게이션 버그 해결 (Phase 7.8)
- [x] 사용자 프로필 기반 개인화 흐름 완성 (Phase 8.1)

### **Future Roadmap**
- **Real Backend**: Mock API를 실제 Spring/Node.js 서버로 마이그레이션
- **Advanced AI**: 사용자 행동 패턴 학습을 통한 추천 알고리즘 고도화
- **Social**: 친구와 챌린지 결과를 공유하는 소셜 기능 추가

---
