package com.example.demovmtests.util

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.example.demovmtests.R
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SimpleDialogProvider
@Inject constructor() {

    fun showInfoDialog(
        activity: Activity,
        title: String,
        message: String,
        isCancelable: Boolean,
        positiveButtonAction: () -> Unit
    ) {
        AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.dialog_ok_button_label) { _, _ -> positiveButtonAction() }
            .setCancelable(isCancelable)
            .create()
            .show()
    }

    fun showInfoDialog(
        activity: Activity,
        @StringRes title: Int,
        @StringRes message: Int
    ) {
        showInfoDialog(
            activity,
            activity.getString(title),
            activity.getString(message),
            isCancelable = true
        ) {}
    }
}