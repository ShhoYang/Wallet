package com.highstreet.lib.view

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import com.highstreet.lib.R

open class RefreshLayout : SwipeRefreshLayout {

    constructor(context: Context) : this(context,null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setColorSchemeResources(R.color.colorPrimary)
    }

    fun stopRefresh() {
        isRefreshing = false
    }
}