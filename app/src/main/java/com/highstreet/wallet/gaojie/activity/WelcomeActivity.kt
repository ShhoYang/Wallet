package com.highstreet.wallet.gaojie.activity

import android.Manifest
import com.highstreet.lib.common.AppManager
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.dao.Account
import com.highstreet.wallet.gaojie.AccountManager
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WelcomeActivity : BaseActivity() {

    override fun showToolbar() = false

    override fun getLayoutId() = R.layout.g_activity_welcome

    override fun initView() {
        AppManager.instance().finishAllActivityExceptAppoint(this)
        GlobalScope.launch(Dispatchers.Main) {
            val account = getAccount()
            requestPermissions(account)
        }
    }

    private fun requestPermissions(account: Account?) {
        RxPermissions(this).request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        ).subscribe {
            if (it) {
                if (account?.address == null || account.address.isEmpty()) {
                    InitWalletActivity.start(this)
                    finish()
                } else {
                    to(MainActivity::class.java, true)
                }
            } else {
                AppManager.instance().exit()
            }
        }
    }

    private suspend fun getAccount(): Account? {
        return withContext(Dispatchers.IO) {
            AccountManager.instance().init()
            AccountManager.instance().account
        }
    }
}
