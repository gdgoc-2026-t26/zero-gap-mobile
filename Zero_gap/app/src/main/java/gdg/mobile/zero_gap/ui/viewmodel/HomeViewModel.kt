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

    private val _showAlert = MutableStateFlow(true)
    val showAlert: StateFlow<Boolean> = _showAlert.asStateFlow()

    private val _todayDiary = MutableStateFlow<String?>(null)
    val todayDiary: StateFlow<String?> = _todayDiary.asStateFlow()

    private val _weeklyEmotionScores = MutableStateFlow<List<Int>>(emptyList())
    val weeklyEmotionScores: StateFlow<List<Int>> = _weeklyEmotionScores.asStateFlow()

    fun fetchStats() {
        viewModelScope.launch {
            try {
                val calendar = Calendar.getInstance()
                val endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                calendar.add(Calendar.DAY_OF_YEAR, -7)
                val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                val missionsResponse = NetworkClient.apiService.getMissions(startDate, endDate)
                val emotionsResponse = NetworkClient.apiService.getEmotions(startDate, endDate)

                // 1. Completed missions count
                _completedMissionsCount.value = missionsResponse.missions.count { it.accomplished }

                // 2. Average emotion score
                val scores = emotionsResponse.emotions.map { it.score }
                _averageEmotionScore.value = if (scores.isNotEmpty()) scores.average() else 0.0
                
                // Weekly scores for graph
                _weeklyEmotionScores.value = emotionsResponse.emotions.takeLast(7).map { it.score }

                // 3. Today success
                _isTodaySuccess.value = missionsResponse.missions.any { 
                    it.date == today && it.accomplished 
                }

                // 4. Alert visibility: if any achievement in last 3 days
                val threeDaysAgo = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -3) }.time
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val recentAchievement = missionsResponse.missions.any { mission ->
                    mission.accomplished && mission.date?.let { sdf.parse(it)?.after(threeDaysAgo) } == true
                }
                _showAlert.value = !recentAchievement

                // 5. Today's summary (from backend)
                val summaryResponse = NetworkClient.apiService.getSummary(startDate, endDate)
                _todayDiary.value = summaryResponse.summary

            } catch (e: Exception) {
                // Log error
            }
        }
    }
}
