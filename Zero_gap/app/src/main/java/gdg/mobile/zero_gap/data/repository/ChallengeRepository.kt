package gdg.mobile.zero_gap.data.repository

import gdg.mobile.zero_gap.R
import gdg.mobile.zero_gap.data.model.Challenge
import gdg.mobile.zero_gap.data.model.ChallengeCategory
import gdg.mobile.zero_gap.data.model.MissionPatchRequest
import gdg.mobile.zero_gap.data.network.NetworkClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChallengeRepository {

    suspend fun getRecommendations(duration: String): List<String> {
        val response = NetworkClient.apiService.getTodayMissionRecommendations(duration)
        return response.missionRecommendations
    }

    fun getChallenges(startDate: String, endDate: String): Flow<List<Challenge>> = flow {
        val response = NetworkClient.apiService.getMissions(startDate, endDate)
        val challenges = response.missions.map { dto ->
            Challenge(
                id = dto.id.toString(),
                title = dto.name,
                description = dto.description ?: "",
                iconResId = R.drawable.ic_bolt,
                category = ChallengeCategory.ACTION
            )
        }
        emit(challenges)
    }

    suspend fun completeMission(id: String, accomplished: Boolean, description: String? = null): String {
        val request = MissionPatchRequest(accomplished, description)
        val response = NetworkClient.apiService.completeMission(id, request)
        return response.cheerMessage
    }
}
