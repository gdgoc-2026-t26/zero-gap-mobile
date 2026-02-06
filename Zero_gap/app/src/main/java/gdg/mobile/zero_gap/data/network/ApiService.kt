package gdg.mobile.zero_gap.data.network

import gdg.mobile.zero_gap.data.model.*
import retrofit2.http.*

interface ApiService {
    
    // Missions
    @GET("missions")
    suspend fun getMissions(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): MissionResponse

    @GET("missions/today")
    suspend fun getTodayMissionRecommendations(
        @Query("durationInSeconds") durationInSeconds: Int
    ): TodayMissionResponse

    @POST("missions")
    suspend fun registerMission(
        @Body request: MissionDTO
    ): MissionRegistrationResponse

    @PATCH("missions/{id}")
    suspend fun completeMission(
        @Path("id") id: Int,
        @Body request: Map<String, Any> // For accomplished and description
    ): MissionSuccessResponse

    // Emotions
    @GET("emotions")
    suspend fun getEmotions(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): EmotionResponse

    @POST("emotions")
    suspend fun registerEmotion(
        @Body request: EmotionDTO
    ): Map<String, Int> // Returns { "id": Int }

    // Summary
    @GET("summary")
    suspend fun getSummary(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): SummaryResponse
}
