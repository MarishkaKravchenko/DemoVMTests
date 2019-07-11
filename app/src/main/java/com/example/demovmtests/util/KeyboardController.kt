package com.example.demovmtests.util

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import javax.inject.Inject

class KeyboardController
@Inject
constructor() {
    fun hideKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        activity.currentFocus?.let { view ->
            view.windowToken?.let { windowToken ->
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
            }
        }
    }
}