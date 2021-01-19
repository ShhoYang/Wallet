package com.highstreet.wallet.gaojie.activity

import com.highstreet.lib.ui.BaseActivity
import com.highstreet.wallet.BuildConfig
import com.highstreet.wallet.R
import kotlinx.android.synthetic.main.g_activity_about.*


/**
 * @author Yang Shihao
 * @Date 12/18/20
 */
class AboutActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.g_activity_about

    override fun initView() {
        title = "关于${getString(R.string.wallet_app_name)}"
        if (BuildConfig.testnet) {
            ivLogo.setImageResource(R.mipmap.ic_launcher_2)
        } else {
            ivLogo.setImageResource(R.mipmap.ic_launcher)
        }

        tvVersion.text = "v${packageManager.getPackageInfo(packageName, 0).versionName}"
    }
}