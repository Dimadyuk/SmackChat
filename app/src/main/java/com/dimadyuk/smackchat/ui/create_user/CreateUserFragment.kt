package com.dimadyuk.smackchat.ui.create_user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dimadyuk.smackchat.databinding.FragmentCreateUserBinding

class CreateUserFragment : Fragment() {

    private var _binding: FragmentCreateUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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
        binding.createUserCreateAvatarImageView.setOnClickListener {
            // Handle the click event
        }
        binding.createUserGenerateColorButton.setOnClickListener {
            // Handle the click event
        }
        binding.createUserCreateUserButton.setOnClickListener {
            // Handle the click event
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}