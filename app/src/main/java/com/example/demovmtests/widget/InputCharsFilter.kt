package com.example.demovmtests.widget

import android.text.InputFilter
import android.text.Spanned

class InputCharsFilter(private val digits: CharSequence) : InputFilter {

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        var result = source ?: ""
        val digitsString = digits.toString()
        source?.let {
            it.forEach { char ->
                if (!digitsString.contains(char)) {
                    result = result.toString().replace(char.toString(), "", false)
                }
            }
        }
        return result
    }
}