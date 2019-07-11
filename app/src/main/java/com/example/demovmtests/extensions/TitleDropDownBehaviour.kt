package com.example.demovmtests.extensions

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.view.ContextThemeWrapper
import com.example.demovmtests.R
import com.example.demovmtests.widget.TextInputView
import kotlinx.android.synthetic.main.dialog_select_title.view.dialog_title_cancel_button as popupCancelButton
import kotlinx.android.synthetic.main.dialog_select_title.view.dialog_title_ok_button as popupOkButton
import kotlinx.android.synthetic.main.dialog_select_title.view.dialog_title_options_list as titleRadioGroup

fun TextInputView.setUpTitleDropDown(onOkClick: () -> Unit) {
    val dialogData = getTitleDialog(context) { selectedTitle ->
        text = selectedTitle
        onOkClick()
    }
    setOnTextClickListener {
        selectTitle(context, dialogData.second, text.toString())
        dialogData.first.show()
    }
    setFocusableInput(false)
}

fun selectTitle(context: Context, dialogView: View, title: String) {
    val positionInList = getTitlePositionInList(context, title)
    if (positionInList != -1) {
        val checkBox = dialogView.titleRadioGroup.getChildAt(positionInList) as RadioButton
        checkBox.isChecked = true
    } else {
        dialogView.titleRadioGroup.clearCheck()
    }
}

private fun getTitleDialog(context: Context, onOkClick: (String) -> Unit): Pair<AlertDialog, View> {
    val dialogView = View.inflate(context, R.layout.dialog_select_title, null)
    val dialog = AlertDialog.Builder(context)
        .setView(dialogView)
        .create()

    dialogView.popupOkButton.setOnClickListener {
        notifyIfCheckedOption(dialogView.titleRadioGroup, onOkClick)
        dialog.cancel()
    }
    dialogView.popupCancelButton.setOnClickListener {
        dialog.cancel()
    }
    populateTitleOptions(context, dialogView.titleRadioGroup)

    return Pair(dialog, dialogView)
}

private fun notifyIfCheckedOption(radioGroup: RadioGroup, onOkClick: (String) -> Unit) {
    val checkedId = radioGroup.checkedRadioButtonId
    if (checkedId != -1) {
        val childView = radioGroup.findViewById(checkedId) as RadioButton?
        childView?.let { radioButton ->
            onOkClick(radioButton.text.toString())
        }
    }
}

private fun getTitlePositionInList(context: Context, title: String): Int {
    val titles = getTitlesList(context)
    return titles.asList().indexOf(title)
}

private fun populateTitleOptions(context: Context, titleRadioGroupView: RadioGroup) {
    val titles = getTitlesList(context)
    val contextThemeWrapper = ContextThemeWrapper(context, R.style.AppTheme_Profile_Title_RadioButton)

    for (title in titles) {
        val radioButton = RadioButton(contextThemeWrapper)
        radioButton.text = title
        titleRadioGroupView.addView(radioButton)
    }
}

private fun getTitlesList(context: Context) = context.resources.getStringArray(R.array.profile_titles)