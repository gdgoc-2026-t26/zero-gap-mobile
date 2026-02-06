package gdg.mobile.zero_gap.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gdg.mobile.zero_gap.data.network.NetworkClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {

    private val _completedMissionsCount = MutableStateFlow(0)
    val completedMissionsCount: StateFlow<Int> = _completedMissionsCount.asStateFlow()

    private val _averageEmotionScore = MutableStateFlow(0.0)
    val averageEmotionScore: StateFlow<Double> = _averageEmotionScore.asStateFlow()

    private val _isTodaySuccess = MutableStateFlow(false)
    val isTodaySuccess: StateFlow<Boolean> = _isTodaySuccess.asStateFlow()

    fun fetchStats() {
        viewModelScope.launch {
            try {
                // Fetch for the current month roughly
                val startDate = "2026-02-01"
                val endDate = "2026-02-28"
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                val missionsResponse = NetworkClient.apiService.getMissions(startDate, endDate)
                val emotionsResponse = NetworkClient.apiService.getEmotions(startDate, endDate)

                // 1. Completed missions count
                _completedMissionsCount.value = missionsResponse.missions.count { it.accomplished }

                // 2. Average emotion score
                val scores = emotionsResponse.emotions.map { it.score }
                _averageEmotionScore.value = if (scores.isNotEmpty()) scores.average() else 0.0

                // 3. Today success (if at least one mission is completed today)
                // In mock mode, we look for today's date in missions
                _isTodaySuccess.value = missionsResponse.missions.any { 
                    it.date == today && it.accomplished 
                }

            } catch (e: Exception) {
                // Log error
            }
        }
    }
}
