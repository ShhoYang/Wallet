package com.highstreet.lib.view.listener

import android.view.View
import android.widget.EditText

/**
 * @author Yang Shihao
 * @date 2019-06-20
 */
object RxView {

    private const val CLICK_INTERVAL = 1000

    fun textChanges2(et1: EditText?, et2: EditText?, block: (String, String) -> Unit) {

        if (et1 == null || et2 == null) {
            return
        }

        et1.addTextChangedListener(object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                block(s.toString(), et2.text.toString())
            }
        })

        et2.addTextChangedListener(object : SimpleTextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                block(et1.text.toString(), s.toString())
            }
        })
    }

    fun textChanges3(et1: EditText?, et2: EditText?, et3: EditText?, block: (String, String, String) -> Unit) {

        if (et1 == null || et2 == null || et3 == null) {
            return
        }

        et1.addTextChangedListener(object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                block(s.toString(), et2.text.toString(), et3.text.toString())
            }
        })

        et2.addTextChangedListener(object : SimpleTextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                block(et1.text.toString(), s.toString(), et3.text.toString())
            }
        })

        et3.addTextChangedListener(object : SimpleTextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                block(et1.text.toString(), et2.text.toString(), s.toString())
            }
        })
    }

    fun textChanges(vararg et: EditText, block: () -> Unit) {
        et.forEach {
            it.addTextChangedListener(object : SimpleTextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    block()
                }
            })
        }
    }

    fun click(view: View?, block: () -> Unit) {
        if (view == null) {
            return
        }
        var pre = 0L
        view.setOnClickListener {
            val c = System.currentTimeMillis()
            if (c - pre >= CLICK_INTERVAL) {
                pre = c
                block()
            }
        }
    }

    fun click(view: View?, listener: View.OnClickListener) {
        if (view == null) {
            return
        }
        var pre = 0L
        view.setOnClickListener {
            val c = System.currentTimeMillis()
            if (c - pre >= CLICK_INTERVAL) {
                pre = c
                listener.onClick(view)
            }
        }
    }

    fun click(view: View?, interval: Int, listener: View.OnClickListener) {
        if (view == null) {
            return
        }
        var pre = 0L
        view.setOnClickListener {
            val c = System.currentTimeMillis()
            if (c - pre >= interval) {
                pre = c
                listener.onClick(view)
            }
        }
    }
}