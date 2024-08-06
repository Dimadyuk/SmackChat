package com.dimadyuk.smackchat.controller.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dimadyuk.smackchat.R
import com.dimadyuk.smackchat.databinding.FragmentLoginBinding
import com.dimadyuk.smackchat.services.AuthService
import com.dimadyuk.smackchat.utilities.hideKeyboard

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
        binding.loginProgressBar.visibility = View.INVISIBLE
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
            showProgressBar(true)
            val email = binding.loginEmailText.text.toString()
            val password = binding.loginPasswordText.text.toString()
            requireActivity().hideKeyboard()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    context,
                    "Please fill in both email and password",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            AuthService.loginUser(requireContext(), email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(requireContext()) { findSuccess ->
                        if (findSuccess) {
                            val navController = findNavController()
                            navController.navigate(R.id.nav_home)
                            showProgressBar(false)
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
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
    private fun errorToast() {
        Toast.makeText(
            context,
            "Something went wrong, please try again",
            Toast.LENGTH_LONG
        ).show()
        showProgressBar(false)
    }

    private fun showProgressBar(enable: Boolean) {
        with(binding) {
            if (enable) {
                loginProgressBar.visibility = View.VISIBLE
            } else {
                loginProgressBar.visibility = View.INVISIBLE
            }
            loginCreateUserButton.isEnabled = !enable
            loginLoginButton.isEnabled = !enable
        }
    }
}
