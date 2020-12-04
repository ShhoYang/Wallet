package com.highstreet.lib.ui

import android.app.Activity
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author Yang Shihao
 * @date 2018/11/18
 */
abstract class BaseFragment : Fragment() {

    /**
     * 懒加载标记
     */
    private var isLazy = false
    private var isLoad = false

    private lateinit var fragmentRootView: View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fragmentRootView = inflater.inflate(getLayoutId(), container, false)
        return fragmentRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepare(savedInstanceState)
        initView()
        if (!isLazy) {
            initData()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isLazy && !isLoad) {
            initData()
            isLoad = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoad = false
    }

    fun <T : View> f(id: Int): T? {
        return fragmentRootView.findViewById(id)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    open fun prepare(savedInstanceState: Bundle?) {

    }

    open fun initView() {

    }

    open fun initData() {

    }

    fun lazyLoad(b: Boolean = true) {
        this.isLazy = b
    }

    private fun act(block: (BaseActivity) -> Unit) {
        val activity = activity
        if (activity != null && activity is BaseActivity && !activity.isFinishing) {
            block(activity)
        }
    }

    fun showLoading() {
        act { it.showLoading() }
    }

    fun hideLoading() {
        act { it.hideLoading() }
    }

    fun toast(msg: String?) {
        act { it.toast(msg) }
    }

    fun toast(@StringRes resId: Int) {
        act { it.toast(resId) }
    }

    fun to(cls: Class<out Activity>, isFinish: Boolean = false) {
        act { it.to(cls, isFinish) }
    }
}