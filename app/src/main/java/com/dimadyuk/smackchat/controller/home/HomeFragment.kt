package com.dimadyuk.smackchat.controller.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dimadyuk.smackchat.databinding.FragmentHomeBinding
import com.dimadyuk.smackchat.services.MessageService
import com.dimadyuk.smackchat.utilities.hideKeyboard

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setClickListeners()
        setObservers()


        return root
    }

    private fun setObservers() {
        val textView: TextView = binding.chanelName
        MessageService.selectedChannelLiveData.observe(viewLifecycleOwner) {
            textView.text = it?.name
        }
    }

    private fun setClickListeners() {
        binding.sendMessageButton.setOnClickListener {
            requireActivity().hideKeyboard()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
