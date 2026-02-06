package gdg.mobile.zero_gap.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gdg.mobile.zero_gap.data.repository.ChallengeRepository
import gdg.mobile.zero_gap.ui.viewmodel.ChallengeViewModel
import gdg.mobile.zero_gap.ui.viewmodel.HomeViewModel

class ViewModelFactory(
    private val repository: ChallengeRepository,
    private val context: android.content.Context? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChallengeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChallengeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel() as T
        }
        if (modelClass.isAssignableFrom(gdg.mobile.zero_gap.ui.viewmodel.AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val sessionManager = gdg.mobile.zero_gap.data.auth.SessionManager(context!!)
            return gdg.mobile.zero_gap.ui.viewmodel.AuthViewModel(
                gdg.mobile.zero_gap.data.repository.AuthRepository(),
                sessionManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
