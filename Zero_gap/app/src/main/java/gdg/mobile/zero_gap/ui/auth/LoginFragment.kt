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
import gdg.mobile.zero_gap.databinding.FragmentLoginBinding
import gdg.mobile.zero_gap.ui.ViewModelFactory
import gdg.mobile.zero_gap.ui.viewmodel.AuthViewModel
import gdg.mobile.zero_gap.data.repository.ChallengeRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory(ChallengeRepository(), requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(requireContext(), "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvGoToSignup.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signup)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collectLatest { state ->
                when (state) {
                    is AuthViewModel.AuthState.Loading -> {
                        binding.btnLogin.isEnabled = false
                        binding.btnLogin.text = "로그인 중..."
                    }
                    is AuthViewModel.AuthState.Success -> {
                        Toast.makeText(requireContext(), "로그인 성공!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.navigation_home)
                    }
                    is AuthViewModel.AuthState.Error -> {
                        binding.btnLogin.isEnabled = true
                        binding.btnLogin.text = "로그인"
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
