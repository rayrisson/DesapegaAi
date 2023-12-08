package com.example.desapegaai.utilities

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.Locale

open class EditTextUtilities {
    fun addDecimalLimiter(editText: EditText, maxLimit: Int = 2) {
        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                val str = editText.text!!.toString()
                if (str.isEmpty()) return
                val str2 = decimalLimiter(str, maxLimit)

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

    private fun decimalLimiter(string: String, MAX_DECIMAL: Int): String {
        val str = string.replace(".", "")
        return "%,.0f".format(Locale.GERMAN, str.toDouble())


//        var str = string
//        if (str[0] == '.') str = "0$str"
//        val max = str.length
//
//        var rFinal = ""
//        var after = false
//        var i = 0
//        var up = 0
//        var decimal = 0
//        var t: Char
//
//        val decimalCount = str.count{ ".".contains(it) }
//
//        if (decimalCount > 1)
//            return str.dropLast(1)
//
//        while (i < max) {
//            t = str[i]
//            if (t != '.' && !after) {
//                up++
//            } else if (t == '.') {
//                after = true
//            } else {
//                decimal++
//                if (decimal > MAX_DECIMAL)
//                    return rFinal
//            }
//            rFinal += t
//            i++
//        }
//        return rFinal
    }
}