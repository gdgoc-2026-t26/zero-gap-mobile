package gdg.mobile.zero_gap.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import gdg.mobile.zero_gap.R
import gdg.mobile.zero_gap.data.model.ProfileDTO
import gdg.mobile.zero_gap.databinding.FragmentSignupBinding
import gdg.mobile.zero_gap.ui.ViewModelFactory
import gdg.mobile.zero_gap.ui.viewmodel.AuthViewModel
import gdg.mobile.zero_gap.data.repository.ChallengeRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory(ChallengeRepository(), requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignup.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val name = binding.etName.text.toString()
            val password = binding.etPassword.text.toString()

            // Collect Profile Data
            val job = getSelectedChipText(binding.chipGroupJob)
            val focus = getSelectedChipText(binding.chipGroupFocus)
            val trait = getSelectedChipText(binding.chipGroupTrait) // e.g. "계획적"
            val goal = getSelectedChipText(binding.chipGroupGoal)   // e.g. "몰입"
            val interests = getSelectedInterests()

            if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty()) {
                if (job.isEmpty() || focus.isEmpty() || trait.isEmpty() || goal.isEmpty()) {
                    Toast.makeText(requireContext(), "모든 프로필 항목을 선택해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val profile = ProfileDTO(job, focus, trait, goal, interests)
                viewModel.signupAndSetupProfile(email, name, password, profile)

            } else {
                Toast.makeText(requireContext(), "기본 정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signup_to_login)
        }

        observeViewModel()
    }

    private fun getSelectedChipText(chipGroup: ChipGroup): String {
        val chipId = chipGroup.checkedChipId
        return if (chipId != View.NO_ID) {
            chipGroup.findViewById<Chip>(chipId).text.toString()
        } else {
            ""
        }
    }

    private fun getSelectedInterests(): List<String> {
        val interests = mutableListOf<String>()
        binding.chipGroupInterests.checkedChipIds.forEach { id ->
             binding.chipGroupInterests.findViewById<Chip>(id)?.let {
                 interests.add(it.text.toString())
             }
        }
        return interests
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collectLatest { state ->
                when (state) {
                    is AuthViewModel.AuthState.Loading -> {
                        binding.btnSignup.isEnabled = false
                        binding.btnSignup.text = "프로필 저장 중..."
                    }
                    is AuthViewModel.AuthState.Success -> {
                        Toast.makeText(requireContext(), "환영합니다! 맞춤 설정이 완료되었습니다.", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.navigation_home)
                    }
                    is AuthViewModel.AuthState.Error -> {
                        binding.btnSignup.isEnabled = true
                        binding.btnSignup.text = "시작하기 (프로필 저장)"
                        Toast.makeText(requireContext(), "에러: ${state.message}", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
