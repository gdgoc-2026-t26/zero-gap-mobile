package gdg.mobile.zero_gap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import gdg.mobile.zero_gap.databinding.FragmentDiaryBinding

import androidx.lifecycle.lifecycleScope
import gdg.mobile.zero_gap.data.model.EmotionRequest
import gdg.mobile.zero_gap.data.network.NetworkClient
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import java.text.SimpleDateFormat
import java.util.*

class DiaryFragment : Fragment() {
    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSave.setOnClickListener {
            saveDiary()
        }
    }

    private fun saveDiary() {
        val score = when (binding.rgEmotion.checkedRadioButtonId) {
            gdg.mobile.zero_gap.R.id.rb1 -> 1
            gdg.mobile.zero_gap.R.id.rb2 -> 2
            gdg.mobile.zero_gap.R.id.rb3 -> 3
            gdg.mobile.zero_gap.R.id.rb4 -> 4
            gdg.mobile.zero_gap.R.id.rb5 -> 5
            else -> 3
        }
        val content = binding.etContent.text.toString()
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val emotionRequest = EmotionRequest(
            score = score,
            date = date,
            description = content
        )

        lifecycleScope.launch {
            try {
                binding.btnSave.isEnabled = false
                NetworkClient.apiService.registerEmotion(emotionRequest)
                Toast.makeText(requireContext(), "일기가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "저장 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.btnSave.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
