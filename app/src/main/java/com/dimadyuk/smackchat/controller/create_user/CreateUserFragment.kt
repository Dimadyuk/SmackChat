package com.dimadyuk.smackchat.controller.create_user

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dimadyuk.smackchat.databinding.FragmentCreateUserBinding
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
                // Handle the click event
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
