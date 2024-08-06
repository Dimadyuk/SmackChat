package com.dimadyuk.smackchat.utilities

import android.app.Activity
import android.view.inputmethod.InputMethodManager

class Utils

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}
