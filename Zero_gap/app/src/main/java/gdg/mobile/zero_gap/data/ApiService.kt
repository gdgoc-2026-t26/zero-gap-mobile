package gdg.mobile.zero_gap.data

import retrofit2.http.GET

interface ApiService {
    // Placeholder endpoint
    @GET("ping")
    suspend fun ping(): String
}
