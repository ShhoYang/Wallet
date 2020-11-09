package com.highstreet.wallet.gaojie.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.highstreet.lib.extensions.init
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.adapter.MnemonicAdapter
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.highstreet.wallet.utils.WKey
import com.highstreet.wallet.utils.WUtil
import kotlinx.android.synthetic.main.g_activity_backup.*

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
class BackupActivity : BaseActivity() {

    private var from = FROM_CREATE
    private var id = 0L
    private lateinit var entropy: String

    override fun prepare(savedInstanceState: Bundle?) {
        from = intent.getIntExtra(ExtraKey.INT, FROM_CREATE)
        id = intent.getLongExtra(ExtraKey.LONG, 0L)
        entropy = intent.getStringExtra(ExtraKey.STRING) ?: ""
    }

    override fun getLayoutId() = R.layout.g_activity_backup

    override fun initView() {
        title = "立即备份"
        val list = ArrayList(WKey.getRandomMnemonic(WUtil.HexStringToByteArray(entropy)))
        rv.init(MnemonicAdapter(list), 4)
        RxView.click(btnNext) {
            BackupVerifyActivity.start(this, from, id, entropy)
            finish()
        }
    }

    override fun onBackPressed() {
        if (FROM_CREATE == from) {
            go(MainActivity::class.java, true)
        } else {
            finish()
        }
    }

    companion object {

        const val FROM_CREATE = 1
        const val FROM_WALLET_MANAGER = 2

        fun start(context: Context, from: Int, id: Long, entropy: String) {
            val intent = Intent(context, BackupActivity::class.java)
            intent.putExtra(ExtraKey.INT, from)
            intent.putExtra(ExtraKey.LONG, id)
            intent.putExtra(ExtraKey.STRING, entropy)
            context.startActivity(intent)
        }
    }
}