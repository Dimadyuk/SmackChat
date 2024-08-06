package com.dimadyuk.smackchat.controller.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dimadyuk.smackchat.R
import com.dimadyuk.smackchat.databinding.FragmentLoginBinding
import com.dimadyuk.smackchat.services.AuthService

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val loginViewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setClickListeners()
        setObservers(loginViewModel)

        return root
    }

    private fun setObservers(loginViewModel: LoginViewModel) {
        val textView: TextView = binding.loginDontHaveAccountText
        loginViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

    private fun setClickListeners() {
        binding.loginLoginButton.setOnClickListener {
            val email = binding.loginEmailText.text.toString()
            val password = binding.loginPasswordText.text.toString()
            AuthService.loginUser(requireContext(), email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(requireContext()) { findSuccess ->
                        if (findSuccess) {
                            val navController = findNavController()
                            navController.navigate(R.id.nav_home)
                        }
                    }
                }
            }
        }
        binding.loginCreateUserButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.nav_create_user)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
