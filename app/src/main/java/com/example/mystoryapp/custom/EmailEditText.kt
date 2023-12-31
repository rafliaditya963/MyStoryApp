package com.example.mystoryapp.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.mystoryapp.R

class EmailEditText : AppCompatEditText {
    private lateinit var inactiveBackground: Drawable
    private lateinit var validBackground: Drawable
    private lateinit var invalidBackground: Drawable
    private lateinit var emailImage: Drawable
    var valid: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        validBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_valid) as Drawable
        invalidBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_invalid) as Drawable
        inactiveBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_inactive) as Drawable
        emailImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_email_24) as Drawable

        background = inactiveBackground

        setButtonDrawables()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                validateEmail(s)
            }
        })
    }

    private fun setButtonDrawables(
        start: Drawable? = emailImage,
        top: Drawable? = null,
        end: Drawable? = null,
        bottom: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom)
    }

    private fun validateEmail(email: CharSequence?) {
        if (email != null) {
            valid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        background = if (valid) validBackground else invalidBackground
        if (!valid) error = resources.getString(R.string.invalid_email)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = resources.getString(R.string.email)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        background = if (focused) {
            if (valid) validBackground else invalidBackground
        } else {
            inactiveBackground
        }
    }
}