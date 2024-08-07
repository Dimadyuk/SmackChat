package com.dimadyuk.smackchat.controller.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dimadyuk.smackchat.controller.App
import com.dimadyuk.smackchat.databinding.FragmentHomeBinding
import com.dimadyuk.smackchat.services.MessageService
import com.dimadyuk.smackchat.services.UserDataService
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
            it?.let {
                MessageService.getMessages(it.id) { complete ->
                    if (complete) {
//                        binding.messageListView.adapter = MessageAdapter(requireContext(), MessageService.messages)
                        for (message in MessageService.messages) {
                            println("Message: ${message.message}")
                        }
                    }
                }
            }
        }
    }

    private fun setClickListeners() {
        binding.sendMessageButton.setOnClickListener {
            if (App.prefs.isLoggedIn && MessageService.selectedChannel
                != null
                && binding.messageText.text?.isNotEmpty() == true
            ) {
                val userId = UserDataService.id
                val channelId = MessageService.selectedChannel!!.id
                App.socket.emit(
                    "newMessage",
                    binding.messageText.text.toString(),
                    userId,
                    channelId,
                    UserDataService.name,
                    UserDataService.avatarName,
                    UserDataService.avatarColor,
                )
                binding.messageText.text?.clear()
            }
            requireActivity().hideKeyboard()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
