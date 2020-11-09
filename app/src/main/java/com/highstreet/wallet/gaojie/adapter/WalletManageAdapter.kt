package com.highstreet.wallet.gaojie.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.highstreet.lib.adapter.BasePagedAdapter
import com.highstreet.lib.adapter.ViewHolder
import com.highstreet.lib.utils.DisplayUtils
import com.highstreet.lib.utils.DrawableUtils
import com.highstreet.wallet.R
import com.highstreet.wallet.dao.Account
import com.highstreet.wallet.gaojie.AccountManager

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */

class WalletManageAdapter() : BasePagedAdapter<Account>(R.layout.g_item_wallet_manage) {

    private lateinit var normalDrawable: Drawable
    private lateinit var selectedDrawable: Drawable
    private lateinit var backupDrawable: Drawable
    private var currentAccount: Account? = null

    constructor(context: Context) : this() {
        val width = DisplayUtils.dp2px(context, 1)
        val radius = DisplayUtils.dp2px(context, 6).toFloat()
        normalDrawable = DrawableUtils.generateRoundRectBorderDrawable(radius, width, ContextCompat.getColor(context, R.color.line))
        selectedDrawable = DrawableUtils.generateRoundRectBorderDrawable(radius, width, ContextCompat.getColor(context, R.color.colorPrimary))
        backupDrawable = DrawableUtils.generateRoundRectBorderDrawable(100.0F, width, ContextCompat.getColor(context, R.color.text_orange))
        currentAccount = AccountManager.instance().account
    }

    override fun bindViewHolder(holder: ViewHolder, item: Account, position: Int) {
        holder.getView<ConstraintLayout>(R.id.clContent).background = if (item.address == currentAccount?.address) {
            holder.visible(R.id.ivSelected)
            selectedDrawable
        } else {
            holder.gone(R.id.ivSelected)
            normalDrawable
        }

        holder.getView<TextView>(R.id.tvBackup).background = backupDrawable

//        if (item.isBackup) {
//            holder.gone(R.id.tvBackup)
//        } else {
//            holder.visible(R.id.tvBackup)
//        }

        holder.setText(R.id.tvWalletName, item.nickName)
                .setText(R.id.tvWalletAddress, item.address)
                .setText(R.id.tvAmount, item.address)

        holder.setClickListener(arrayOf(R.id.tvWalletAddress, R.id.ivCopy, R.id.tvBackup,R.id.ivDelete)) {
            itemClickListener?.itemClicked(it, item, position)
        }
    }

    fun setCurrentAccount(account: Account?) {
        this.currentAccount = account
    }
}