package com.highstreet.wallet.gaojie.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.highstreet.lib.common.AppManager
import com.highstreet.lib.extensions.visibility
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.constant.ExtraKey
import kotlinx.android.synthetic.main.g_activity_init_wallet.*
import java.lang.StringBuilder


/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
class InitWalletActivity : BaseActivity(), View.OnClickListener {

    private var isAdd = false

    override fun prepare(savedInstanceState: Bundle?) {
        isAdd = intent.getBooleanExtra(ExtraKey.BOOLEAN, false)
    }

    override fun showToolbar() = false

    override fun getLayoutId() = R.layout.g_activity_init_wallet

    override fun initView() {
        getToolbar()?.visibility(isAdd)
        RxView.click(btnCreate) {
//            start(TO_DO_TYPE_CREATE)
            CreateWalletActivity.start(this, "", isAdd)
        }

        RxView.click(btnImport) {
//            start(TO_DO_TYPE_IMPORT)
            ImportWalletActivity.start(this, "", isAdd)
        }

//        Left.setOnClickListener(this)
//        Center.setOnClickListener(this)
//        Right.setOnClickListener(this)

    }

    private fun start(toDoType: Int) {
        WalletTypeActivity.start(this, toDoType, intent.getBooleanExtra(ExtraKey.BOOLEAN, false))
    }

    override fun initData() {
        if (AccountManager.instance().accounts.isEmpty()) {
            AppManager.instance().finishAllActivityExceptAppoint(this)
        }
    }


    override fun onClick(v: View?) {
        when (v) {
            Left -> append("L")

            Center -> append("C")

            Right -> append("R")
        }
    }

    private val sb = StringBuilder()

    private fun append(s: String) {
        sb.append(s)

        if (sb.length >= 12) {
            if ("CCLLRRLCRRCL" == sb.substring(sb.length - 12, sb.length)) {
                to(TestActivity::class.java)
            }
        }
    }

    companion object {

        /**
         * 怎么去创建
         */
        const val TO_DO_TYPE_CREATE = 1
        const val TO_DO_TYPE_IMPORT = 2

        fun start(context: Context, isAdd: Boolean = false) {
            val intent = Intent(context, InitWalletActivity::class.java)
            intent.putExtra(ExtraKey.BOOLEAN, isAdd)
            context.startActivity(intent)
        }
    }
}