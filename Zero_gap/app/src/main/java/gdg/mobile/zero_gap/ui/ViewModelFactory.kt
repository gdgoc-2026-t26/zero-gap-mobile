package gdg.mobile.zero_gap.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gdg.mobile.zero_gap.data.repository.ChallengeRepository
import gdg.mobile.zero_gap.ui.viewmodel.ChallengeViewModel

class ViewModelFactory(private val repository: ChallengeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChallengeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChallengeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
