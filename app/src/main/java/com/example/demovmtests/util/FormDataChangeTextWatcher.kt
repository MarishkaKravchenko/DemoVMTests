package com.example.demovmtests.util

import android.text.Editable
import android.text.TextWatcher
import com.example.demovmtests.base.FormDataChangeCallback

class FormDataChangeTextWatcher<out DATA_TYPE, out DATA_PROVIDER : () -> DATA_TYPE>(
    private val formDataChangeCallback: FormDataChangeCallback<DATA_TYPE>,
    private val dataProvider: DATA_PROVIDER
) : TextWatcher {

    var isEnabled = true

    override fun afterTextChanged(s: Editable?) {
        if (isEnabled) {
            formDataChangeCallback.onFormDataChanged(dataProvider.invoke())
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // NOOP
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // NOOP
    }
}