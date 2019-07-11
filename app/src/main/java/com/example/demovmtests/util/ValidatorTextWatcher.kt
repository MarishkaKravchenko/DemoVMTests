package com.example.demovmtests.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.example.demovmtests.base.FormDataChangeCallback

class ValidatorTextWatcher<out DATA_TYPE, out DATA_PROVIDER : () -> DATA_TYPE>(
    private val formDataChangeCallback: FormDataChangeCallback<DATA_TYPE>,
    editText: EditText,
    private val dataProvider: DATA_PROVIDER
) : TextWatcher {
    private val capitalizingTextWatcher = CapitalizingTextWatcher(editText)

    var isEnabled = true

    override fun afterTextChanged(s: Editable?) {
        if (isEnabled) {
            capitalizingTextWatcher.afterTextChanged(s)
            formDataChangeCallback.onFormDataChanged(dataProvider.invoke())
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Do nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Do nothing
    }
}