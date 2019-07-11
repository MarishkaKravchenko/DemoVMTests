package com.example.demovmtests.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import com.example.demovmtests.R
import kotlinx.android.synthetic.main.widget_status_view.view.status_description as descriptionView
import kotlinx.android.synthetic.main.widget_status_view.view.status_image as imageView
import kotlinx.android.synthetic.main.widget_status_view.view.status_primary_button as primaryButton
import kotlinx.android.synthetic.main.widget_status_view.view.status_secondary_button as secondaryButton
import kotlinx.android.synthetic.main.widget_status_view.view.status_title as titleView

class StatusView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    var title: String
        get() = titleView.text.toString()
        set(value) {
            titleView.text = value
        }

    var icon: Int = 0
        set(value) {
            field = value
            imageView.setImageResource(value)
        }

    var description: String
        get() = descriptionView.text.toString()
        set(value) {
            descriptionView.text = value
        }

    var primaryButtonText: String
        get() = primaryButton.text.toString()
        set(value) {
            primaryButton.text = value
        }

    var secondaryButtonVisibility: Int
        get() = secondaryButton.visibility
        set(value) {
            secondaryButton.visibility = value
        }

    var primaryButtonClickListener: View.OnClickListener? = null
        set(value) {
            field = value
            primaryButton.setOnClickListener(value)
        }

    fun showConnectionErrorStatus(retry: () -> Unit) {
        title = context.getString(R.string.no_connection_prompt_title)
        description = context.getString(R.string.no_connection_prompt_description)
        icon = R.drawable.status_internet
        primaryButtonText = context.getString(R.string.try_again_button)
        secondaryButtonVisibility = View.GONE
        visibility = View.VISIBLE
        primaryButtonClickListener = OnClickListener { retry.invoke() }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_status_view, this)
        setBackgroundResource(R.drawable.background_dialog_transfer)
        orientation = VERTICAL
        isClickable = true
    }
}