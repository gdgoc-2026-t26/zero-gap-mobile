package gdg.mobile.zero_gap.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkClient {
    private const val BASE_URL = "http://34.50.17.186/"
    private const val USE_MOCK = false // Switched to real API

    var tokenProvider: (() -> String?)? = null

        val original = chain.request()
        val url = original.url
        val path = url.encodedPath
        
        val requestBuilder = original.newBuilder()
            .header("Accept", "application/json")
            .header("User-Agent", "ZeroGap-Android")
        
        // Skip auth header for signup and login
        val skipAuth = path.endsWith("/users") || path.endsWith("users") || 
                      path.contains("/auth/login") || path.contains("auth/login")
        
        if (!skipAuth) {
            val token = tokenProvider?.invoke()
            if (token != null) {
                requestBuilder.header("Authorization", "Bearer $token")
            }
        }
        
        chain.proceed(requestBuilder.build())
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = if (USE_MOCK) {
        MockApiService()
    } else {
        retrofit.create(ApiService::class.java)
    }
}
