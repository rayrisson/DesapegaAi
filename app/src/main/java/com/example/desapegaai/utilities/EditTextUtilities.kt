package com.example.desapegaai.utilities

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.Locale

open class EditTextUtilities {
    fun addDecimalLimiter(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                val str = editText.text!!.toString()
                if (str.isEmpty()) return
                val str2 = formatString(str)

                if (str2 != str) {
                    editText.setText(str2)
                    val pos = editText.text!!.length
                    editText.setSelection(pos)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

    private fun formatString(string: String): String {
        val str = string.replace(".", "")
        return "%,.0f".format(Locale.GERMAN, str.toDouble())
    }
}