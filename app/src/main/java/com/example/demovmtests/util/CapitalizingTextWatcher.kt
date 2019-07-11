package com.example.demovmtests.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

private const val WORD_DELIMITER = " "

class CapitalizingTextWatcher(private val editText: EditText) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        val capitalizedFirstLetters = capitalizeFirstLetters(s.toString())

        s?.let { editable ->
            updateTextIfChangedMaintainingSelection(editable, capitalizedFirstLetters)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // NOOP
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // NOOP
    }

    private fun capitalizeFirstLetters(originalString: String) =
        originalString
            .split(WORD_DELIMITER)
            .joinToString(WORD_DELIMITER) {
                it.toLowerCase().capitalize()
            }

    private fun updateTextIfChangedMaintainingSelection(editable: Editable, capitalizedFirstLetters: String) {
        if (editable.toString() == capitalizedFirstLetters) return

        val currentSelectionStart = editText.selectionStart
        val currentSelectionEnd = editText.selectionEnd

        editable.clear()
        editable.insert(0, capitalizedFirstLetters)

        if (editText.text.isNotEmpty()) {
            editText.setSelection(currentSelectionStart, currentSelectionEnd)
        }
    }
}