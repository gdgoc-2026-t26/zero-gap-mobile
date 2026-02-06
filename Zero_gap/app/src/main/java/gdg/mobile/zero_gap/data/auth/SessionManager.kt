package gdg.mobile.zero_gap.data.auth

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("zero_gap_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val USER_TOKEN = "user_token"
    }

    fun saveAuthToken(token: String) {
        prefs.edit().putString(USER_TOKEN, token).apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun clearAuthToken() {
        prefs.edit().remove(USER_TOKEN).apply()
    }

    fun isLoggedIn(): Boolean {
        return fetchAuthToken() != null
    }
}
