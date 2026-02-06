package gdg.mobile.zero_gap.data.network

import gdg.mobile.zero_gap.data.model.*
import kotlinx.coroutines.delay

class MockApiService : ApiService {

    enum class Scenario {
        PRODUCTIVE, // 고득점 + 미션 완료 많음
        STRUGGLING, // 저득점 + 미션 미완료 많음
        NEW_USER    // 데이터 거의 없음
    }

    companion object {
        private var currentScenario = Scenario.PRODUCTIVE
        
        // Dynamic data storage for mock mode
        private val mockMissions = mutableListOf<MissionResponse>()
        private val mockEmotions = mutableListOf<EmotionResponse>()
        private var initialized = false

        private fun initializeData() {
            if (initialized) return
            when (currentScenario) {
                Scenario.PRODUCTIVE -> {
                    mockMissions.addAll(listOf(
                        MissionResponse("uuid-1", "매일 10분 스트레칭", "2026-02-05", true, "아침 공기 마시며 스트레칭 완료!"),
                        MissionResponse("uuid-2", "코테 1문제 풀기", "2026-02-06", true, "골드 문제 해결!"),
                        MissionResponse("uuid-3", "독서 30분", "2026-02-07", false, null)
                    ))
                    mockEmotions.addAll(listOf(
                        EmotionResponse(1L, 5, "2026-02-07", "성취감이 엄청난 하루였다!"),
                        EmotionResponse(2L, 4, "2026-02-06", "꾸준히 잘 하고 있는 것 같아 뿌듯하다."),
                        EmotionResponse(3L, 5, "2026-02-05", "날씨도 좋고 모든 게 완벽했다.")
                    ))
                }
                Scenario.STRUGGLING -> {
                    mockMissions.addAll(listOf(
                        MissionResponse("uuid-4", "침대 정리하기", "2026-02-07", false, null),
                        MissionResponse("uuid-5", "물 1L 마시기", "2026-02-07", false, null)
                    ))
                    mockEmotions.addAll(listOf(
                        EmotionResponse(1L, 2, "2026-02-07", "오늘은 좀 의욕이 없네..."),
                        EmotionResponse(2L, 1, "2026-02-06", "많이 지치는 날이었다. 휴식이 필요해."),
                        EmotionResponse(3L, 2, "2026-02-05", "생각대로 잘 안 풀리는 한 주다.")
                    ))
                }
                Scenario.NEW_USER -> {}
            }
            initialized = true
        }
    }

    init {
        initializeData()
    }

    override suspend fun getMissions(startDate: String, endDate: String): MissionListResponse {
        delay(500)
        return MissionListResponse(missions = mockMissions.toList())
    }

    override suspend fun getTodayMissionRecommendations(durationInSeconds: Int?): MissionRecommendationResponse {
        delay(800)
        // Map seconds to buckets roughly
        val recommendations = when (currentScenario) {
            Scenario.PRODUCTIVE -> when {
                (durationInSeconds ?: 0) <= 600 -> listOf("고난도 알고리즘 퀴즈", "스쿼트 50개", "기술 블로그 읽기")
                (durationInSeconds ?: 0) <= 2400 -> listOf("사이드 프로젝트 코딩", "영어 아티클 번역", "중강도 웨이트 트레이닝")
                else -> listOf("새로운 언어 학습", "전체 코드 리팩토링", "러닝 5km")
            }
            else -> when {
                (durationInSeconds ?: 0) <= 600 -> listOf("가벼운 명상", "물 한 잔 마시기", "창문 열고 환기하기")
                (durationInSeconds ?: 0) <= 2400 -> listOf("방 정리하기", "좋아하는 노래 듣기", "일기 한 줄 쓰기")
                else -> listOf("짧은 산책", "따뜻한 차 마시기", "좋아하는 영화 보기")
            }
        }
        return MissionRecommendationResponse(missionRecommendations = recommendations)
    }

    override suspend fun registerMission(request: MissionCreateRequest): MissionCreateResponse {
        delay(500)
        // Idempotency check: if mission with same name and date exists, return existing
        val existing = mockMissions.find { it.name == request.name && it.date == request.date }
        if (existing != null) {
            return MissionCreateResponse(existing.id ?: "", "이미 등록된 도전입니다!")
        }

        val newId = "uuid-${mockMissions.size + 1}"
        mockMissions.add(MissionResponse(newId, request.name, request.date, false, null))
        return MissionCreateResponse(newId, "새로운 도전을 응원합니다!")
    }

    override suspend fun completeMission(missionId: String, request: MissionPatchRequest): MissionPatchResponse {
        delay(500)
        val index = mockMissions.indexOfFirst { it.id == missionId }
        if (index != -1) {
            val mission = mockMissions[index]
            mockMissions[index] = mission.copy(
                accomplished = request.accomplished,
                description = request.description
            )
        }
        return MissionPatchResponse("축하합니다! 오늘도 한 걸음 성장하셨네요.")
    }

    override suspend fun getSummary(startDate: String, endDate: String): SummaryResponse {
        delay(1000)
        val summary = when (currentScenario) {
            Scenario.PRODUCTIVE -> "당신의 에너지가 최고조입니다! 지금처럼 멋진 발걸음을 유지해 보세요."
            Scenario.STRUGGLING -> "조금 쉼표가 필요한 시기네요. 무리하지 말고 아주 작은 것부터 시작해 봐요."
            Scenario.NEW_USER -> "제로갭에 오신 것을 환영합니다! 오늘부터 당신만의 기록을 채워나가 보세요."
        }
        return SummaryResponse(summary = summary)
    }

    override suspend fun getEmotions(startDate: String, endDate: String): EmotionListResponse {
        delay(500)
        return EmotionListResponse(emotions = mockEmotions.toList())
    }

    override suspend fun registerEmotion(request: EmotionRequest): EmotionCreateResponse {
        delay(500)
        mockEmotions.removeIf { it.date == request.date }
        val newId = (mockEmotions.maxByOrNull { it.id ?: 0L }?.id ?: 0L) + 1L
        mockEmotions.add(EmotionResponse(newId, request.score, request.date, request.description))
        return EmotionCreateResponse(newId)
    }

    override suspend fun signup(request: UserSignUpRequest): UserResponse {
        delay(500)
        return UserResponse(1L, request.name, request.email)
    }

    override suspend fun login(request: LoginRequest): LoginResponse {
        delay(500)
        return LoginResponse(accessToken = "mock_token_123")
    }

    override suspend fun getMyInfo(): UserResponse {
        delay(500)
        return UserResponse(1L, "사용자", "user@example.com")
    }
}
