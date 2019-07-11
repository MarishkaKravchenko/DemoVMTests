package com.example.demovmtests.extensions

import android.app.DatePickerDialog
import com.example.demovmtests.widget.TextInputView


fun TextInputView.setUpDateDropDown(dateProvider: DateProvider, maxDate: Long, onOkClick: (Int, Int, Int) -> Unit) {

    setOnTextClickListener {
        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth -> onOkClick(year, month, dayOfMonth) },
            dateProvider.getYear(),
            dateProvider.getMonth(),
            dateProvider.getDayOfMonth()
        )
        datePickerDialog.datePicker.maxDate = maxDate
        datePickerDialog.show()
    }
    setFocusableInput(false)
}

interface DateProvider {
    fun getDayOfMonth(): Int
    fun getMonth(): Int
    fun getYear(): Int
}