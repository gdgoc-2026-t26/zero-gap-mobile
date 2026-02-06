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
            findNavController().navigate(gdg.mobile.zero_gap.R.id.action_home_to_diary)
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
    }

    private fun updateChallengeCards(recommendations: List<String>) {
        // Since we have a static UI with 3 chips, we map the list to them
        if (recommendations.isNotEmpty()) {
            binding.chipChallengeAction.text = recommendations.getOrNull(0) ?: "공백"
            binding.chipChallengeEmotion.text = recommendations.getOrNull(1) ?: "공백"
            binding.chipChallengeMindset.text = recommendations.getOrNull(2) ?: "공백"
            
            // Show cards if they were hidden, or just update text
            binding.cardChallengeAction.visibility = if (recommendations.size > 0) View.VISIBLE else View.GONE
            binding.cardChallengeEmotion.visibility = if (recommendations.size > 1) View.VISIBLE else View.GONE
            binding.cardChallengeMindset.visibility = if (recommendations.size > 2) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
