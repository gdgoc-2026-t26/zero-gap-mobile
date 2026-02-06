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
import gdg.mobile.zero_gap.R
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

            if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty()) {
                viewModel.signup(email, name, password)
            } else {
                Toast.makeText(requireContext(), "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signup_to_login)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collectLatest { state ->
                when (state) {
                    is AuthViewModel.AuthState.Loading -> {
                        binding.btnSignup.isEnabled = false
                        binding.btnSignup.text = "처리 중..."
                    }
                    is AuthViewModel.AuthState.Success -> {
                        Toast.makeText(requireContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show()
                        // Next step would be Profile Setup, but for now go to Home
                        findNavController().navigate(R.id.navigation_home)
                    }
                    is AuthViewModel.AuthState.Error -> {
                        binding.btnSignup.isEnabled = true
                        binding.btnSignup.text = "시작하기"
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
