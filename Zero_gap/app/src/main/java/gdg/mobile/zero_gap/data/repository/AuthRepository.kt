package gdg.mobile.zero_gap.data.repository

import gdg.mobile.zero_gap.data.model.*
import gdg.mobile.zero_gap.data.network.NetworkClient

class AuthRepository {
    private val api = NetworkClient.apiService

    suspend fun signup(request: UserSignUpRequest): UserResponse {
        return api.signup(request)
    }

    suspend fun login(request: LoginRequest): LoginResponse {
        return api.login(request)
    }

}
