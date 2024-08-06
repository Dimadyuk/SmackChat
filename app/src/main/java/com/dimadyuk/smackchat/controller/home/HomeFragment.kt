package com.dimadyuk.smackchat.controller.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dimadyuk.smackchat.databinding.FragmentHomeBinding
import com.dimadyuk.smackchat.utilities.hideKeyboard

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setClickListeners()
        setObservers(homeViewModel)


        return root
    }

    private fun setObservers(homeViewModel: HomeViewModel) {
        val textView: TextView = binding.mainChanelName
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
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
