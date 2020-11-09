package com.highstreet.lib.ui

import android.support.v4.app.Fragment
import com.highstreet.lib.R

abstract class ContainerActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_container

    override fun initView() {
        supportFragmentManager.beginTransaction()
                .add(R.id.frameLayout, getFragment())
                .commit()
    }

    abstract fun getFragment(): Fragment
}
