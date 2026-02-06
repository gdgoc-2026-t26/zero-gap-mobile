package gdg.mobile.zero_gap.data.model

import com.google.gson.annotations.SerializedName

data class MissionDTO(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("name") val name: String,
    @SerializedName("date") val date: String,
    @SerializedName("accomplished") val accomplished: Boolean = false,
    @SerializedName("description") val description: String? = null
)

data class MissionResponse(
    @SerializedName("missions") val missions: List<MissionDTO>
)

data class TodayMissionResponse(
    @SerializedName("missionRecommendations") val missionRecommendations: List<String>
)

data class MissionRegistrationResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("cheerMessage") val cheerMessage: String
)

data class MissionSuccessResponse(
    @SerializedName("cheerMessage") val cheerMessage: String
)

data class EmotionDTO(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("score") val score: Int,
    @SerializedName("date") val date: String,
    @SerializedName("description") val description: String
)

data class EmotionResponse(
    @SerializedName("emotions") val emotions: List<EmotionDTO>
)

data class SummaryResponse(
    @SerializedName("summary") val summary: String
)
