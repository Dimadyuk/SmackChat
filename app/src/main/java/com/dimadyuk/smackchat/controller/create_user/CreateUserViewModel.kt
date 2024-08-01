package com.dimadyuk.smackchat.controller.create_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreateUserViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Tap to generate user avatar"
    }
    val text: LiveData<String> = _text
}
