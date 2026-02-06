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
                // UserResponse doesn't have token, normally you'd login after signup or signup returns token
                // Backend UserResponse doesn't have token based on Swagger, so we might need a separate login call or it depends on backend behavior.
                // For now, if mock returns UserResponse, we simulate success.
                _authState.value = AuthState.Success("signup_success_token")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "signup failed")
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
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "login failed")
            }
        }
    }

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                val profile = repository.getProfile()
                _profileState.value = profile
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    fun signupAndSetupProfile(email: String, name: String, pass: String, profile: ProfileDTO) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                // 1. Signup
                val signupResponse = repository.signup(UserSignUpRequest(email, pass, name))
                // In real world, maybe login after signup to get token
                val loginResponse = repository.login(LoginRequest(email, pass))
                val token = loginResponse.accessToken
                sessionManager.saveAuthToken(token)

                // 2. Update Profile
                repository.updateProfile(profile)
                
                // 3. Update local profile state
                _profileState.value = profile

                _authState.value = AuthState.Success(token)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Signup and setup failed")
            }
        }
    }

    fun updateProfile(profile: ProfileDTO) {
        viewModelScope.launch {
            try {
                repository.updateProfile(profile)
                _profileState.value = profile
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val token: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
