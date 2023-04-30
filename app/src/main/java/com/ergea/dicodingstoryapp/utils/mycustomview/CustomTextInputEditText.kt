package com.ergea.dicodingstoryapp.utils.mycustomview

import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.ergea.dicodingstoryapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

class CustomTextInputEditText : TextInputEditText {

    private lateinit var textInputLayout: TextInputLayout

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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textInputLayout = parent.parent as TextInputLayout
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            val delay: Long = 1000
            val handler: Handler = Handler(Looper.getMainLooper())

            private val inputFinishChecker = Runnable {
                when (inputType) {
                    0x00000021 -> {
                        if (!Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
                            textInputLayout.error =
                                context.getString(R.string.email_invalid_format)
                        }
                    }
                    0x00000081 -> {
                        if (text.toString().length < 8) {
                            textInputLayout.error =
                                context.getString(R.string.password_invalid_format)
                        }
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textInputLayout.isErrorEnabled = false
                handler.removeCallbacks(inputFinishChecker)
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    handler.postDelayed(inputFinishChecker, delay)
                }
            }

        })
    }

}