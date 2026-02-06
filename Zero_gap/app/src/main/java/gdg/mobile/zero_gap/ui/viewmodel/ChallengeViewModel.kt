package gdg.mobile.zero_gap.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gdg.mobile.zero_gap.data.model.Challenge
import gdg.mobile.zero_gap.data.network.NetworkClient
import gdg.mobile.zero_gap.data.repository.ChallengeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChallengeViewModel(private val repository: ChallengeRepository) : ViewModel() {

    private val _challenges = MutableStateFlow<List<Challenge>>(emptyList())
    val challenges: StateFlow<List<Challenge>> = _challenges.asStateFlow()

    private val _selectedChallenge = MutableStateFlow<Challenge?>(null)
    val selectedChallenge: StateFlow<Challenge?> = _selectedChallenge.asStateFlow()

    private val _recommendations = MutableStateFlow<List<String>>(emptyList())
    val recommendations: StateFlow<List<String>> = _recommendations.asStateFlow()

    private val _mentalEnergy = MutableStateFlow(0)
    val mentalEnergy: StateFlow<Int> = _mentalEnergy.asStateFlow()

    fun fetchChallenges(startDate: String, endDate: String) {
        viewModelScope.launch {
            repository.getChallenges(startDate, endDate).collect {
                _challenges.value = it
            }
        }
    }

    fun fetchMentalEnergy() {
        viewModelScope.launch {
            try {
                // Derived from average emotion score * 20 (assuming 1-5 scale)
                val response = NetworkClient.apiService.getEmotions("2026-02-01", "2026-02-28")
                val emotions = response.emotions
                val average = if (emotions.isNotEmpty()) emotions.map { it.score }.average() else 3.0
                _mentalEnergy.value = (average * 20).toInt().coerceIn(0, 100)
            } catch (e: Exception) {
                _mentalEnergy.value = 50
            }
        }
    }

    fun fetchRecommendations(durationMinutes: Int) {
        viewModelScope.launch {
            try {
                val durationEnum = when {
                    durationMinutes <= 10 -> "SHORT"
                    durationMinutes <= 40 -> "MEDIUM"
                    else -> "LONG"
                }
                _recommendations.value = repository.getRecommendations(durationEnum)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun selectChallenge(challenge: Challenge) {
        _selectedChallenge.value = challenge
    }
}
