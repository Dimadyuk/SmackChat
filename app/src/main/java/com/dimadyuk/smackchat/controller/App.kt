package com.dimadyuk.smackchat.controller

import android.app.Application
import com.dimadyuk.smackchat.utilities.Constants.SOCKET_URL
import com.dimadyuk.smackchat.utilities.SharedPrefs
import io.socket.client.IO
import io.socket.client.Socket

class App : Application() {

    companion object {
        lateinit var prefs: SharedPrefs
        lateinit var socket: Socket
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        socket = IO.socket(SOCKET_URL)
        super.onCreate()
    }
}
