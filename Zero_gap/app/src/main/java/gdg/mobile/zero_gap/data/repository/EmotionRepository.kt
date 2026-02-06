package gdg.mobile.zero_gap.data.repository

import gdg.mobile.zero_gap.data.model.EmotionRequest
import gdg.mobile.zero_gap.data.model.EmotionResponse
import gdg.mobile.zero_gap.data.network.NetworkClient

class EmotionRepository {
    suspend fun getEmotions(startDate: String, endDate: String): List<EmotionResponse> {
        val response = NetworkClient.apiService.getEmotions(startDate, endDate)
        return response.emotions
    }

    suspend fun registerEmotion(score: Int, description: String, date: String): Long {
        val request = EmotionRequest(score, date, description)
        val response = NetworkClient.apiService.registerEmotion(request)
        return response.id
    }
}
