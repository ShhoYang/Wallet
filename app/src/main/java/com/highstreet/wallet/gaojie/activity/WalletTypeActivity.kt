package com.highstreet.wallet.gaojie.activity

import android.content.Context
import android.content.Intent
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.constant.ExtraKey
import kotlinx.android.synthetic.main.g_activity_wallet_type.*

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
class WalletTypeActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.g_activity_wallet_type

    override fun initView() {
        title = "选择钱包类型"
        RxView.click(clDip) {

            val toDoType = intent.getIntExtra(ExtraKey.INT, InitWalletActivity.TO_DO_TYPE_CREATE)
            val isAdd = intent.getBooleanExtra(ExtraKey.BOOLEAN, false)

            if (InitWalletActivity.TO_DO_TYPE_CREATE == toDoType) {
                CreateWalletActivity.start(this, "", isAdd)
                finish()
            } else {
                ImportWalletActivity.start(this, "", isAdd)
                finish()
            }
        }
    }

    companion object {

        fun start(context: Context, toDoType: Int, isAdd: Boolean = false) {
            val intent = Intent(context, WalletTypeActivity::class.java)
            intent.putExtra(ExtraKey.INT, toDoType)
            intent.putExtra(ExtraKey.BOOLEAN, isAdd)
            context.startActivity(intent)
        }
    }
}