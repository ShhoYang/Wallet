package com.highstreet.lib.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.Gravity
import com.highstreet.lib.R
import com.highstreet.lib.utils.DisplayUtils

/**
 * @author Yang Shihao
 * @Date 2020/4/16
 *
 * 朴素按钮
 */
class RoundButton : AppCompatTextView {

    private val normalState =
            intArrayOf(-android.R.attr.state_pressed, android.R.attr.state_enabled)
    private val pressedState =
            intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
    private val disableState = intArrayOf(-android.R.attr.state_enabled)

    private var radius = 0.0F
    private var normalColor = 0
    private var pressedColor = 0
    private var disableColor = 0
    private var btnTextColor = Color.WHITE

    constructor(context: Context) : super(context) {
        initUI(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initUI(context, attrs)
    }

    private fun initUI(context: Context, attrs: AttributeSet?) {
        isClickable = true
        textSize = 14.0F
        paint.isFakeBoldText = true
        gravity = Gravity.CENTER
        context.obtainStyledAttributes(attrs, R.styleable.RoundButton).apply {
            radius = getDimension(R.styleable.RoundButton_radius, DisplayUtils.dp2px(context, 6).toFloat())
            normalColor = getColor(R.styleable.RoundButton_normalColor, ContextCompat.getColor(context, R.color.colorPrimary))
            pressedColor = getColor(R.styleable.RoundButton_pressedColor, ContextCompat.getColor(context, R.color.colorPrimary))
            disableColor = getColor(R.styleable.RoundButton_disableColor, ContextCompat.getColor(context, R.color.colorPrimary8))
            btnTextColor = getColor(R.styleable.RoundButton_btnTextColor, btnTextColor)
            recycle()
        }
        setTextColor(btnTextColor)
        buildDrawableState()
    }

    private fun buildDrawableState() {
        val outRect =
                floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)

        val stateListDrawable = StateListDrawable()
        val roundRectShape = RoundRectShape(outRect, null, null)

        val normalDrawable = ShapeDrawable(roundRectShape)
        normalDrawable.paint.color = normalColor
        normalDrawable.intrinsicWidth
        stateListDrawable.addState(normalState, normalDrawable)

        val pressedDrawable = ShapeDrawable(roundRectShape)
        pressedDrawable.paint.color = pressedColor
        stateListDrawable.addState(pressedState, pressedDrawable)

        val disableDrawable = ShapeDrawable(roundRectShape)
        disableDrawable.paint.color = disableColor
        stateListDrawable.addState(disableState, disableDrawable)

        background = stateListDrawable
    }
}