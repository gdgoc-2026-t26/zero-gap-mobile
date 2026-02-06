package gdg.mobile.zero_gap.data.repository

import gdg.mobile.zero_gap.data.model.EmotionDTO
import gdg.mobile.zero_gap.data.network.NetworkClient

class EmotionRepository {
    suspend fun getEmotions(startDate: String, endDate: String): List<EmotionDTO> {
        val response = NetworkClient.apiService.getEmotions(startDate, endDate)
        return response.emotions
    }
}
