package com.example.demovmtests.widget

import android.text.InputFilter
import android.text.Spanned

const val ALL_SYMBOLS =
    "〰ℹ️~#^\\|\$%*!@/()';:;,+?{}=!\$^';,?×÷&;&;&;{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪1234567890\u20BD₴¢₴–][‹›ʼʼ«»”«½¼⅓⁴ⁿ⅝⅛¾²⅔⅞⅜⁴ⁿ³"

class InputSymbolFilter(private val hideSymbols: Boolean) : InputFilter {

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        var result = source ?: ""
        if (hideSymbols) {
            source?.let {
                it.forEach { char ->
                    val typeChar = Character.getType(char)
                    if (ALL_SYMBOLS.contains(char) ||
                        typeChar == Character.SURROGATE.toInt() ||
                        typeChar == Character.OTHER_SYMBOL.toInt() ||
                        typeChar == Character.MATH_SYMBOL.toInt() ||
                        typeChar == Character.DIRECTIONALITY_COMMON_NUMBER_SEPARATOR.toInt() ||
                        typeChar == Character.OTHER_PUNCTUATION.toInt()
                    ) {
                        result = result.toString().replace(char.toString(), "", false)
                    }
                }
            }
        }
        return result
    }
}