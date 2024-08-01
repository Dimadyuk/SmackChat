package com.dimadyuk.smackchat.utilities

object Constants {
    const val BASE_URL = "http://localhost:3005/v1/"
    const val URL_REGISTER = "${BASE_URL}account/register"
    const val URL_LOGIN = "${BASE_URL}account/login"
    const val URL_CREATE_USER = "${BASE_URL}user/add"
    const val URL_GET_USER = "${BASE_URL}user/byEmail/"
    const val URL_GET_CHANNELS = "${BASE_URL}channel"
}

