package com.example.demovmtests.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.KeyListener
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.example.demovmtests.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.widget_text_input_view.view.text_input_layout_container as containerView
import kotlinx.android.synthetic.main.widget_text_input_view.view.text_input_layout_input as textInputView

class TextInputView : ConstraintLayout {

    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null
    ) : this(context, attrs, 0)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initView(context)
        attrs?.applyToView(defStyleAttr)
    }

    private val typeFaceDinBold: Typeface? by lazy {
        ResourcesCompat.getFont(context, R.font.din_bold)
    }
    private val typeFaceDinRegular: Typeface? by lazy {
        ResourcesCompat.getFont(context, R.font.din_regular)
    }

    var text: CharSequence?
        get() = textInputView.text
        set(value) {
            textInputView.setText(value)
            val textLength = textInputView.length()
            val selection = if (textLength > maxLength) maxLength else textLength
            textInputView.setSelection(selection)
        }

    var error: CharSequence?
        get() = containerView.error
        set(value) {
            if (value.isNullOrEmpty()) {
                containerView.isErrorEnabled = false
                containerView.typeface = typeFaceDinRegular
                if (!text.isNullOrEmpty()) {
                    containerView.setHintTextAppearance(R.style.AppTheme_HintTextAppearance_Filled)
                }
            } else {
                containerView.typeface = typeFaceDinBold
                containerView.isErrorEnabled = true
                containerView.error = value
            }
        }

    var hint: CharSequence
        get() = containerView.helperText ?: ""
        set(value) {
            containerView.helperText = value
        }

    var title: CharSequence
        get() = containerView.hint.toString()
        set(value) {
            containerView.hint = value.toString()
        }

    var inputType: Int
        get() = textInputView.inputType
        set(value) {
            textInputView.inputType = value
        }

    var maxLength: Int
        get() {
            val lengthFilter = textInputView.filters.find { it is InputFilter.LengthFilter }
            return (lengthFilter as? InputFilter.LengthFilter)?.max ?: -1
        }
        set(max) {
            val lengthFilterPosition =
                textInputView.filters.indexOfFirst { it is InputFilter.LengthFilter }
            if (lengthFilterPosition == -1) {
                textInputView.filters += InputFilter.LengthFilter(max)
            } else {
                val updatedFilters = textInputView.filters
                updatedFilters[lengthFilterPosition] = InputFilter.LengthFilter(max)
                textInputView.filters = updatedFilters
            }
        }

    val compoundDrawables: Array<Drawable>
        get() = textInputView.compoundDrawables

    val textPadding
        get() = textInputView.paddingLeft + textInputView.paddingRight

    val inputView: TextInputEditText
        get() = textInputView

    fun addTextChangedListener(watcher: TextWatcher) = textInputView.addTextChangedListener(watcher)

    fun removeTextChangedListener(watcher: TextWatcher) =
        textInputView.removeTextChangedListener(watcher)

    fun setTextKeepState(text: String) = textInputView.setTextKeepState(text)

    fun setOnTextClickListener(l: (v: View) -> Unit) {
        textInputView.setOnClickListener(l)
    }

    fun setFocusableInput(isFocusable: Boolean) {
        textInputView.isFocusable = isFocusable
        textInputView.isFocusableInTouchMode = isFocusable
    }

    fun setOnTextFocusChangeListener(listener: ((View, Boolean) -> Unit)?) =
        textInputView.setOnFocusChangeListener(listener)

    fun setOnTextTouchListener(listener: (View, MotionEvent) -> Boolean) =
        textInputView.setOnTouchListener(listener)

    fun setCompoundDrawablesRelativeWithIntrinsicBounds(
        @DrawableRes start: Int,
        @DrawableRes top: Int,
        @DrawableRes end: Int,
        @DrawableRes bottom: Int
    ) = textInputView.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)

    private fun setCompoundDrawablesRelativeWithIntrinsicBounds(
        @DrawableRes start: Drawable?,
        @DrawableRes top: Drawable?,
        @DrawableRes end: Drawable?,
        @DrawableRes bottom: Drawable?
    ) = textInputView.setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.widget_text_input_view, this, true)
        initTextInputListener()
    }

    private fun AttributeSet.applyToView(defStyleAttr: Int) {
        val typedArray =
            context.obtainStyledAttributes(this, R.styleable.TextInputView, defStyleAttr, 0)

        title = typedArray.getString(R.styleable.TextInputView_title) ?: ""

        hint = typedArray.getString(R.styleable.TextInputView_android_hint) ?: ""

        textInputView.inputType =
            typedArray.getInt(
                R.styleable.TextInputView_android_inputType,
                EditorInfo.TYPE_CLASS_TEXT
            )

        textInputView.maxLines =
            typedArray.getInt(
                R.styleable.TextInputView_android_maxLines,
                EditorInfo.TYPE_CLASS_TEXT
            )

        textInputView.setText(typedArray.getString(R.styleable.TextInputView_android_text))

        val digits = typedArray.getText(R.styleable.TextInputView_android_digits) ?: ""
        val restrictSpecialCharacters =
            typedArray.getBoolean(R.styleable.TextInputView_restrictSpecialCharacters, false)

        val maxLength =
            typedArray.getInt(R.styleable.TextInputView_android_maxLength, Int.MAX_VALUE)
        textInputView.filters = if (digits.isEmpty()) {
            arrayOf(
                InputFilter.LengthFilter(maxLength),
                InputSymbolFilter(restrictSpecialCharacters)
            )
        } else {
            arrayOf(
                InputFilter.LengthFilter(maxLength),
                InputCharsFilter(digits),
                InputSymbolFilter(restrictSpecialCharacters)
            )
        }

        val contentDescription =
            typedArray.getString(R.styleable.TextInputView_android_contentDescription)
        containerView.contentDescription = contentDescription
        textInputView.contentDescription = contentDescription

        val drawableEnd = typedArray.getDrawable(R.styleable.TextInputView_android_drawableEnd)
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawableEnd, null)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val autofillHints =
                typedArray.getString(R.styleable.TextInputView_android_autofillHints)
            if (autofillHints?.isNotEmpty() == true) {
                textInputView.setAutofillHints(*(autofillHints.split(",").toTypedArray()))
            }
        }

        typedArray.recycle()
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        var returnedState: Parcelable? = null
        superState?.let {
            val savedState = SavedState(superState)
            val childrenStates = SparseArray<Parcelable>()
            savedState.childrenStates = childrenStates
            for (i in 0 until childCount) {
                getChildAt(i).saveHierarchyState(childrenStates)
            }
            returnedState = savedState
        }

        return returnedState
    }

    private fun initTextInputListener() {

        textInputView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                // no-op
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // no-op
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                when {
                    !error.isNullOrEmpty() -> containerView.typeface = typeFaceDinBold
                    !text.isNullOrEmpty() -> {
                        containerView.typeface = typeFaceDinRegular
                        containerView.setHintTextAppearance(R.style.AppTheme_HintTextAppearance_Filled)
                    }
                    else -> {
                        containerView.typeface = typeFaceDinRegular
                        containerView.setHintTextAppearance(R.style.AppTheme_HintTextAppearance_UnFilled)
                    }
                }
            }
        })
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val savedStated = state as SavedState
        super.onRestoreInstanceState(savedStated.superState)

        for (i in 0 until childCount) {
            getChildAt(i).restoreHierarchyState(savedStated.childrenStates)
        }
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    fun setKeyListener(keyListener: KeyListener?) {
        textInputView.keyListener = keyListener
    }

    internal class SavedState : View.BaseSavedState {
        lateinit var childrenStates: SparseArray<Parcelable>

        constructor(superState: Parcelable) : super(superState)

        private constructor(source: Parcel, classLoader: ClassLoader) : super(source) {
            @Suppress("UNCHECKED_CAST")
            childrenStates = source.readSparseArray(classLoader) as SparseArray<Parcelable>
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            @Suppress("UNCHECKED_CAST")
            out.writeSparseArray(childrenStates as SparseArray<Any>)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState?> =
                object : Parcelable.Creator<SavedState?> {
                    override fun createFromParcel(source: Parcel): SavedState? {
                        return SavedState(source, ClassLoader.getSystemClassLoader())
                    }

                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }
}
