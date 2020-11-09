package com.highstreet.lib.view

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.highstreet.lib.extensions.gone
import com.highstreet.lib.extensions.visible
import com.highstreet.lib.R
import kotlinx.android.synthetic.main.state_view.view.*
import kotlin.properties.Delegates

/**
 * @author Yang Shihao
 * @date 2018/11/24
 */
class StateView : FrameLayout {

    private var contentView: View? = null

    var state: Status by Delegates.observable(Status.DISMISS) { _, old, new ->
        if (old != new) {
            when (new) {
                Status.DISMISS -> {
                    contentView?.visible()
                    stateViewLoading.gone()
                    stateViewNoData.gone()
                    stateViewLoadFailed.gone()
                    stateViewNetworkUnavailable.gone()
                }
                Status.LOADING -> {
                    contentView?.gone()
                    stateViewLoading.visible()
                    stateViewNoData.gone()
                    stateViewLoadFailed.gone()
                    stateViewNetworkUnavailable.gone()
                }
                Status.NO_DATA -> {
                    contentView?.gone()
                    stateViewLoading.gone()
                    stateViewNoData.visible()
                    stateViewLoadFailed.gone()
                    stateViewNetworkUnavailable.gone()
                }
                Status.LOAD_FAILED -> {
                    contentView?.gone()
                    stateViewLoading.gone()
                    stateViewNoData.gone()
                    stateViewLoadFailed.visible()
                    stateViewNetworkUnavailable.gone()
                }
                Status.NETWORK_UNAVAILABLE -> {
                    contentView?.gone()
                    stateViewLoading.gone()
                    stateViewNoData.gone()
                    stateViewLoadFailed.gone()
                    stateViewNetworkUnavailable.visible()
                }
            }
        }
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.state_view, this)

    }

    fun setEmptyImageAndText(@DrawableRes resId: Int, text: String) {
        sTvEmpty?.text = text
    }

    fun setEmptyViewListener(listener: View.OnClickListener) {
        stateViewNoData?.setOnClickListener(listener)
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 5) {
            throw IllegalStateException("EmptyView can only have one child view")
        }
        if (childCount == 5) {
            contentView = getChildAt(4)
        }
        state = Status.LOADING
    }

    enum class Status {
        DISMISS, LOADING, NO_DATA, LOAD_FAILED, NETWORK_UNAVAILABLE
    }
}