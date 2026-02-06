package gdg.mobile.zero_gap.data.network

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

interface ApiService {
    // Example endpoints
    @GET("challenges")
    suspend fun getChallenges(): List<Any>

    @POST("diaries")
    suspend fun saveDiary(@Body diary: Any): Any
}
