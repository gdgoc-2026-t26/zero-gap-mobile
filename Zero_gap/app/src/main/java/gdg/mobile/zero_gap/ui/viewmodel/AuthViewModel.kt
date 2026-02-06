package gdg.mobile.zero_gap.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gdg.mobile.zero_gap.data.model.*
import gdg.mobile.zero_gap.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val sessionManager: gdg.mobile.zero_gap.data.auth.SessionManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _profileState = MutableStateFlow<ProfileDTO?>(null)
    val profileState: StateFlow<ProfileDTO?> = _profileState.asStateFlow()

    fun signup(email: String, name: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.signup(UserSignUpRequest(email, pass, name))
                _authState.value = AuthState.Success("signup_success_token")
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _authState.value = AuthState.Error("HTTP ${e.code()}: ${errorBody ?: e.message()}")
            } catch (e: Exception) {
                _authState.value = AuthState.Error("${e.javaClass.simpleName}: ${e.message ?: "signup failed"}")
            }
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = repository.login(LoginRequest(email, pass))
                sessionManager.saveAuthToken(response.accessToken)
                _authState.value = AuthState.Success(response.accessToken)
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _authState.value = AuthState.Error("HTTP ${e.code()}: ${errorBody ?: e.message()}")
            } catch (e: Exception) {
                _authState.value = AuthState.Error("${e.javaClass.simpleName}: ${e.message ?: "login failed"}")
            }
        }
    }

    fun fetchProfile() {
        // Not implemented in current backend
    }

    fun signupAndSetupProfile(email: String, name: String, pass: String, profile: ProfileDTO) {
        // Profile setup not supported in current backend
        signup(email, name, pass)
    }

    fun updateProfile(profile: ProfileDTO) {
        // Not implemented in current backend
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val token: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
