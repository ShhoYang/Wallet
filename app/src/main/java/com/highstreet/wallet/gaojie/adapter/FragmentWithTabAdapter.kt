package com.highstreet.wallet.gaojie.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

/**
 * @author Yang Shihao
 * @date 2020/10/20
 */
class FragmentWithTabAdapter(fm: FragmentManager, private var data: List<Pair<String, Fragment>>) :
    FragmentStatePagerAdapter(fm) {

    var currentFragment: Fragment? = null

    override fun getItem(position: Int) = data[position].second

    override fun getCount() = data.size

    override fun getPageTitle(position: Int) = data[position].first

    override fun setPrimaryItem(container: ViewGroup, position: Int, obj: Any) {
        currentFragment = obj as Fragment
        super.setPrimaryItem(container, position, obj)
    }

    override fun destroyItem(container: View, position: Int, obj: Any) = Unit

    override fun getItemPosition(obj: Any): Int = PagerAdapter.POSITION_NONE
}
