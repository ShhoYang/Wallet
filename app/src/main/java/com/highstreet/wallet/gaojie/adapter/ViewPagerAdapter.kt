package com.highstreet.wallet.gaojie.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
class ViewPagerAdapter(fm: FragmentManager, private val fragments: List<Fragment>) : FragmentPagerAdapter(fm) {

    override fun getCount() = fragments.size

    override fun getItem(position: Int) = fragments[position]

}