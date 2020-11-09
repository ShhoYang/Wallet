package com.highstreet.wallet.gaojie.activity

import android.annotation.SuppressLint
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import kotlinx.android.synthetic.main.g_activity_agreement.*

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
class AgreementActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.g_activity_agreement

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
       getToolbar()?.apply {
           showBack = false
           title = "服务协议"
       }
        baseWebView.loadUrl("https://github.com/haoshiy/Wallet")
        cbAgree.setOnCheckedChangeListener { _, isChecked ->
            btnNext.isEnabled = isChecked
        }

        RxView.click(btnNext) {
            go(InitWalletActivity::class.java, true)
        }
    }
}
