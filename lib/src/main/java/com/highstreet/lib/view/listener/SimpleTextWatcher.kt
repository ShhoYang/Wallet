package com.highstreet.lib.view.listener

import android.text.Editable
import android.text.TextWatcher

/**
 * @author Yang Shihao
 * @date 2019-06-21
 */
interface SimpleTextWatcher : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable?) {}
}
