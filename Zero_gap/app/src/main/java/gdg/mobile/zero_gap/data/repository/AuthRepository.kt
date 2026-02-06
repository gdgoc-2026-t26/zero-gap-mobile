package gdg.mobile.zero_gap.data.repository

import gdg.mobile.zero_gap.data.model.*
import gdg.mobile.zero_gap.data.network.NetworkClient

class AuthRepository {
    private val api = NetworkClient.apiService

    suspend fun signup(request: SignupRequest): AuthResponse {
        return api.signup(request)
    }

    suspend fun login(request: LoginRequest): AuthResponse {
        return api.login(request)
    }

    suspend fun getProfile(): ProfileDTO {
        return api.getProfile()
    }

    suspend fun updateProfile(profile: ProfileDTO): AuthResponse {
        return api.updateProfile(profile)
    }
}
