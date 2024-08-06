package com.dimadyuk.smackchat.services

import android.graphics.Color
import java.util.Scanner

object UserDataService {
    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun logout() {
        id = ""
        avatarColor = ""
        avatarName = ""
        email = ""
        name = ""
        AuthService.authToken = ""
        AuthService.isLoggedIn = false
        AuthService.userEmail = ""
    }

    fun returnAvatarColor(components: String): Int {
        var r = 0
        var g = 0
        var b = 0
        val strippedColor = components
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")
        val scaner = Scanner(strippedColor)
        if (scaner.hasNext()) {
            r = (scaner.nextDouble() * 255).toInt()
            g = (scaner.nextDouble() * 255).toInt()
            b = (scaner.nextDouble() * 255).toInt()
        }
        return Color.rgb(r, g, b)
    }
}
