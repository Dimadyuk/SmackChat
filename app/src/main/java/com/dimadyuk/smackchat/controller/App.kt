package com.dimadyuk.smackchat.controller

import android.app.Application
import com.dimadyuk.smackchat.utilities.SharedPrefs

class App : Application() {

    companion object {
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}
