package gdg.mobile.zero_gap.data.network

import gdg.mobile.zero_gap.data.model.*
import retrofit2.http.*

interface ApiService {
    
    // Auth & Profile
    @POST("users")
    suspend fun signup(@Body request: UserSignUpRequest): UserResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun getMyInfo(): UserResponse

    // Missions
    @GET("missions")
    suspend fun getMissions(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): MissionListResponse

    @GET("missions/today")
    suspend fun getTodayMissionRecommendations(
        @Query("durationInSeconds") durationInSeconds: Int? = null
    ): MissionRecommendationResponse

    @POST("missions")
    suspend fun registerMission(
        @Body request: MissionCreateRequest
    ): MissionCreateResponse

    @PATCH("missions/{missionId}")
    suspend fun completeMission(
        @Path("missionId") missionId: String,
        @Body request: MissionPatchRequest
    ): MissionPatchResponse

    // Summary
    @GET("summary")
    suspend fun getSummary(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): SummaryResponse

    // Emotions
    @GET("emotions")
    suspend fun getEmotions(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): EmotionListResponse

    @POST("emotions")
    suspend fun registerEmotion(
        @Body request: EmotionRequest
    ): EmotionCreateResponse
}
