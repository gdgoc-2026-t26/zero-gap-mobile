package gdg.mobile.zero_gap.data.model

import com.google.gson.annotations.SerializedName

data class MissionResponse(
    @SerializedName("id") val id: String? = null, // UUID string
    @SerializedName("name") val name: String,
    @SerializedName("date") val date: String,
    @SerializedName("accomplished") val accomplished: Boolean = false,
    @SerializedName("description") val description: String? = null
)

data class MissionCreateRequest(
    @SerializedName("name") val name: String,
    @SerializedName("date") val date: String
)

data class MissionCreateResponse(
    @SerializedName("id") val id: String,
    @SerializedName("cheerMessage") val cheerMessage: String
)

data class MissionPatchRequest(
    @SerializedName("accomplished") val accomplished: Boolean,
    @SerializedName("description") val description: String? = null
)

data class MissionPatchResponse(
    @SerializedName("cheerMessage") val cheerMessage: String
)

data class MissionListResponse(
    @SerializedName("missions") val missions: List<MissionResponse>
)

data class MissionRecommendationResponse(
    @SerializedName("missionRecommendations") val missionRecommendations: List<String>
)

data class EmotionRequest(
    @SerializedName("score") val score: Int,
    @SerializedName("date") val date: String,
    @SerializedName("description") val description: String? = null
)

data class EmotionResponse(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("score") val score: Int,
    @SerializedName("date") val date: String,
    @SerializedName("description") val description: String? = null
)

data class EmotionCreateResponse(
    @SerializedName("id") val id: Long
)

data class EmotionListResponse(
    @SerializedName("emotions") val emotions: List<EmotionResponse>
)

data class SummaryResponse(
    @SerializedName("summary") val summary: String
)


data class UserSignUpRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String
)

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("accessToken") val accessToken: String
)

data class UserResponse(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("name") val name: String? = null
)

// Retained current profile DTO as it's not in swagger yet but used in app

data class ProfileDTO(
    @SerializedName("job") val job: String,
    @SerializedName("focus") val focus: String,
    @SerializedName("trait") val trait: String,
    @SerializedName("goal") val goal: String,
    @SerializedName("interests") val interests: List<String>
)
