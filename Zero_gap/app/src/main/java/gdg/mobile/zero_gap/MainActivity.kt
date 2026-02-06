package gdg.mobile.zero_gap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import gdg.mobile.zero_gap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        
        // Use standard NavComponent integration.
        // This handles selection syncing and top-level navigation naturally.
        navView.setupWithNavController(navController)

        // Check if user is logged in
        val sessionManager = gdg.mobile.zero_gap.data.auth.SessionManager(this)
        
        // Provide token to NetworkClient
        gdg.mobile.zero_gap.data.network.NetworkClient.tokenProvider = {
            sessionManager.fetchAuthToken()
        }

        if (!sessionManager.isLoggedIn()) {
            navController.navigate(R.id.navigation_login)
        }
    }
}