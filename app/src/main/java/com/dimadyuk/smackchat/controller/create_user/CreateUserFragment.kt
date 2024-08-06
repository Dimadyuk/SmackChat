package com.dimadyuk.smackchat.controller.create_user

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import com.dimadyuk.smackchat.R
import com.dimadyuk.smackchat.databinding.FragmentCreateUserBinding
import com.dimadyuk.smackchat.services.AuthService
import com.dimadyuk.smackchat.utilities.Constants.BROADCAST_USER_DATA_CHANGE
import java.util.Random

class CreateUserFragment : Fragment() {

    private var _binding: FragmentCreateUserBinding? = null

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val createUserViewModel =
            ViewModelProvider(this).get(CreateUserViewModel::class.java)

        _binding = FragmentCreateUserBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.createUserProgressBar.visibility = View.INVISIBLE
        setClickListeners()
        setObservers(createUserViewModel)

        return root
    }

    private fun setObservers(createUserViewModel: CreateUserViewModel) {
        val textView: TextView = binding.createUserGenerateTextView
        createUserViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

    private fun setClickListeners() {
        with(binding) {
            createUserCreateAvatarImageView.setOnClickListener {
                val random = Random()
                val color = random.nextInt(2)
                val avatar = random.nextInt(28)
                userAvatar = if (color == 0) {
                    "light$avatar"
                } else {
                    "dark$avatar"
                }
                val imageId = resources.getIdentifier(userAvatar, "drawable", activity?.packageName)
                createUserCreateAvatarImageView.setImageResource(imageId)
            }
            createUserGenerateColorButton.setOnClickListener {
                val random = Random()
                val r = random.nextInt(255)
                val g = random.nextInt(255)
                val b = random.nextInt(255)

                createUserCreateAvatarImageView.setBackgroundColor(
                    Color.rgb(r, g, b)
                )
                val savedR = r.toDouble() / 255
                val savedG = g.toDouble() / 255
                val savedB = b.toDouble() / 255
                avatarColor = "[$savedR, $savedG, $savedB, 1]"
            }
            createUserCreateUserButton.setOnClickListener {
                createUser()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun createUser() {
        val navController = requireActivity()
            .findNavController(R.id.nav_host_fragment_content_main)
        showProgressBar(true)
        with(binding) {
            val userName = createUserNameText.text.toString()
            val email = createUserEmailText.text.toString()
            val password = createUserPasswordText.text.toString()
            if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    context,
                    "Please fill in all fields",
                    Toast.LENGTH_LONG
                ).show()
                showProgressBar(false)
                return
            }
            AuthService.registerUser(
                context = requireContext(),
                email = email,
                password = password
            ) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(
                        context = requireContext(),
                        email = email,
                        password = password
                    ) { loginSuccess ->
                        if (loginSuccess) {
                            AuthService.createUser(
                                context = requireContext(),
                                name = userName,
                                email = email,
                                avatarName = userAvatar,
                                avatarColor = avatarColor
                            ) { createSuccess ->
                                showProgressBar(false)
                                if (createSuccess) {
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager
                                        .getInstance(requireContext())
                                        .sendBroadcast(userDataChange)
                                    navController.popBackStack(R.id.nav_home, false)
                                } else {
                                    errorToast()
                                }
                            }
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        }
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
                createUserProgressBar.visibility = View.VISIBLE
            } else {
                createUserProgressBar.visibility = View.INVISIBLE
            }
            createUserCreateUserButton.isEnabled = !enable
            createUserCreateAvatarImageView.isEnabled = !enable
            createUserGenerateColorButton.isEnabled = !enable
        }
    }
}
