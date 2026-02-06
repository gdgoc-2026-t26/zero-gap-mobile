package gdg.mobile.zero_gap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import gdg.mobile.zero_gap.databinding.FragmentChallengeBinding

import androidx.lifecycle.lifecycleScope
import gdg.mobile.zero_gap.data.network.NetworkClient
import gdg.mobile.zero_gap.data.model.MissionResponse
import gdg.mobile.zero_gap.databinding.ItemChallengeBinding
import kotlinx.coroutines.launch
import android.widget.Toast
import kotlinx.coroutines.flow.collectLatest
import androidx.fragment.app.viewModels
import gdg.mobile.zero_gap.data.repository.ChallengeRepository
import gdg.mobile.zero_gap.ui.viewmodel.ChallengeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChallengeFragment : Fragment() {
    private var _binding: FragmentChallengeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChallengeViewModel by viewModels {
        ViewModelFactory(ChallengeRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Listen for challenge addition requests from Home (One-time event)
        setFragmentResultListener("ADD_MISSION_REQUEST") { _, bundle ->
            bundle.getString("mission_title")?.let { title ->
                registerAndFetch(title)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initial fetch
        fetchMissions()
        
        observeViewModel()
        viewModel.fetchMentalEnergy()
    }

    private fun registerAndFetch(title: String) {
        lifecycleScope.launch {
            try {
                // Register the mission via repository/API
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val request = gdg.mobile.zero_gap.data.model.MissionCreateRequest(
                    name = title,
                    date = today
                )
                NetworkClient.apiService.registerMission(request)
                fetchMissions() // Refresh list
            } catch (e: Exception) {
                Toast.makeText(context, "미션 등록 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                fetchMissions()
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mentalEnergy.collectLatest { energy ->
                binding.tvMentalEnergy.text = "${energy}% 안정적임"
                binding.indicatorMentalEnergy.progress = energy
            }
        }
    }

    private fun fetchMissions() {
        val startDate = "2026-02-01"
        val endDate = "2026-02-28"
        
        lifecycleScope.launch {
            try {
                val response = NetworkClient.apiService.getMissions(startDate, endDate)
                populateMissionList(response.missions)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "미션 로딩 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateMissionList(missions: List<MissionResponse>) {
        val listContainer = binding.root.findViewById<android.widget.LinearLayout>(gdg.mobile.zero_gap.R.id.challengeListContainer) ?: return
        listContainer.removeAllViews()
        
        missions.forEach { mission ->
            val itemBinding = ItemChallengeBinding.inflate(layoutInflater, listContainer, false)
            itemBinding.tvTitle.text = mission.name
            
            val statusTextView = itemBinding.root.findViewById<android.widget.TextView>(gdg.mobile.zero_gap.R.id.tvDescription)
            if (mission.accomplished) {
                statusTextView?.text = "완료되었습니다!"
                itemBinding.root.setCardBackgroundColor(android.graphics.Color.parseColor("#F3F4F6"))
                itemBinding.root.isEnabled = false
                itemBinding.root.alpha = 0.6f
            } else {
                statusTextView?.text = "진행 중입니다."
                itemBinding.root.setCardBackgroundColor(android.graphics.Color.WHITE)
                itemBinding.root.isEnabled = true
                itemBinding.root.alpha = 1.0f
            }
            
            // Apply scale-on-click only for unaccomplished
            if (!mission.accomplished) {
                itemBinding.root.stateListAnimator = android.animation.AnimatorInflater.loadStateListAnimator(
                    requireContext(), 
                    gdg.mobile.zero_gap.R.animator.click_feedback
                )
            } else {
                itemBinding.root.stateListAnimator = null
            }

            itemBinding.root.setOnClickListener {
                if (!mission.accomplished) {
                    completeMission(mission)
                }
            }
            
            listContainer.addView(itemBinding.root)
        }
    }

    private fun completeMission(mission: MissionResponse) {
        val id = mission.id ?: return
        lifecycleScope.launch {
            try {
                val request = gdg.mobile.zero_gap.data.model.MissionPatchRequest(
                    accomplished = true,
                    description = "미션을 완료했습니다!"
                )
                val response = NetworkClient.apiService.completeMission(id, request)
                Toast.makeText(requireContext(), response.cheerMessage, Toast.LENGTH_LONG).show()
                fetchMissions() // Refresh
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "미션 완료 처리 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
