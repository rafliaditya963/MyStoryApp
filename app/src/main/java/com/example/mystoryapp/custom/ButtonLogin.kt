package com.example.mystoryapp.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.mystoryapp.R

class ButtonLogin : AppCompatButton {
    private lateinit var validBackground: Drawable
    private lateinit var invalidBackground: Drawable

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
        validBackground = ContextCompat.getDrawable(context, R.drawable.bg_valid_login) as Drawable
        invalidBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_invalid_login) as Drawable

        background = invalidBackground
        isEnabled = false
    }

    fun changeStatus(status: Boolean) {
        background = if (status) validBackground else invalidBackground
    }
}