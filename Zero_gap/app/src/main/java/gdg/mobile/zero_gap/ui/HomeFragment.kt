package gdg.mobile.zero_gap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import gdg.mobile.zero_gap.data.repository.ChallengeRepository
import androidx.navigation.fragment.findNavController
import gdg.mobile.zero_gap.databinding.FragmentHomeBinding
import gdg.mobile.zero_gap.ui.viewmodel.ChallengeViewModel

import androidx.lifecycle.lifecycleScope
import gdg.mobile.zero_gap.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChallengeViewModel by viewModels {
        ViewModelFactory(ChallengeRepository())
    }

    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory(ChallengeRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChips()
        observeViewModel()
        
        // Auto-load initial 5m missions
        viewModel.fetchRecommendations(5)
        homeViewModel.fetchStats()
        
        binding.btnRecordEmotion.setOnClickListener {
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(gdg.mobile.zero_gap.R.id.navigation_home, false, true)
                .build()
            findNavController().navigate(gdg.mobile.zero_gap.R.id.navigation_diary, null, navOptions)
        }

        binding.imgProfile.setOnClickListener {
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(gdg.mobile.zero_gap.R.id.navigation_home, false, true)
                .build()
            findNavController().navigate(gdg.mobile.zero_gap.R.id.navigation_my, null, navOptions)
        }
    }

    private fun setupChips() {
        binding.chipGroupDuration.setOnCheckedStateChangeListener { group, checkedIds ->
            val duration = when (checkedIds.firstOrNull()) {
                gdg.mobile.zero_gap.R.id.chip5m -> 5
                gdg.mobile.zero_gap.R.id.chip30m -> 30
                gdg.mobile.zero_gap.R.id.chip1h -> 60
                else -> null
            }
            duration?.let {
                viewModel.fetchRecommendations(it)
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recommendations.collectLatest { recommendations ->
                updateChallengeCards(recommendations)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.completedMissionsCount.collectLatest { count ->
                binding.tvCompletedMissions.text = "${count}개"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.averageEmotionScore.collectLatest { score ->
                binding.tvAverageEmotion.text = String.format("%.1f", score)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.isTodaySuccess.collectLatest { isSuccess ->
                binding.tvTodaySuccess.text = if (isSuccess) "YES" else "NO"
                binding.imgTodaySuccess.imageTintList = android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor(if (isSuccess) "#34D399" else "#94A3B8")
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.showAlert.collectLatest { show ->
                binding.alertReengagement.visibility = if (show) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.todayDiary.collectLatest { content ->
                binding.tvTodayDiary.text = content ?: "오늘의 일기를 작성해보세요!"
                binding.tvTodayDiary.alpha = if (content != null) 1.0f else 0.5f
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.weeklyEmotionScores.collectLatest { scores ->
                updateEmotionGraph(scores)
            }
        }
    }

    private fun updateEmotionGraph(scores: List<Int>) {
        val bars = listOf(
            binding.barMon, binding.barTue, binding.barWed, 
            binding.barThu, binding.barFri, binding.barSat, binding.barSun
        )
        // Map scores to heights (e.g., score 1 = 20dp, 5 = 100dp)
        scores.forEachIndexed { index, score ->
            if (index < bars.size) {
                val params = bars[index].layoutParams
                params.height = (score * 20).toPx() // Changed to toPx and reduced scale
                bars[index].layoutParams = params
            }
        }
    }

    private fun Int.toPx(): Int = (this * resources.displayMetrics.density).toInt()

    private fun updateChallengeCards(recommendations: List<String>) {
        mappingChallengeClick(binding.cardChallengeAction, recommendations.getOrNull(0))
        mappingChallengeClick(binding.cardChallengeEmotion, recommendations.getOrNull(1))
        mappingChallengeClick(binding.cardChallengeMindset, recommendations.getOrNull(2))

        if (recommendations.isNotEmpty()) {
            binding.chipChallengeAction.text = recommendations.getOrNull(0) ?: "공백"
            binding.chipChallengeEmotion.text = recommendations.getOrNull(1) ?: "공백"
            binding.chipChallengeMindset.text = recommendations.getOrNull(2) ?: "공백"
            
            binding.cardChallengeAction.visibility = if (recommendations.size > 0) View.VISIBLE else View.GONE
            binding.cardChallengeEmotion.visibility = if (recommendations.size > 1) View.VISIBLE else View.GONE
            binding.cardChallengeMindset.visibility = if (recommendations.size > 2) View.VISIBLE else View.GONE
        }
    }

    private fun mappingChallengeClick(card: View, missionTitle: String?) {
        card.setOnClickListener {
            missionTitle?.let { title ->
                val bundle = Bundle().apply {
                    putString("mission_title", title)
                }
                
                // Mirror the NavOptions used by NavigationUI for tab switches
                val navOptions = androidx.navigation.NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .setPopUpTo(gdg.mobile.zero_gap.R.id.navigation_home, false, true)
                    .build()
                    
                findNavController().navigate(gdg.mobile.zero_gap.R.id.navigation_challenge, bundle, navOptions)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
