package com.highstreet.wallet.gaojie.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.highstreet.lib.adapter.OnItemClickListener
import com.highstreet.lib.extensions.init
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.lib.viewmodel.RxBus
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.adapter.MnemonicAdapter
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.highstreet.wallet.gaojie.event.RefreshWalletListEvent
import com.highstreet.wallet.utils.WKey
import com.highstreet.wallet.utils.WUtil
import kotlinx.android.synthetic.main.g_activity_backup_verify.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
class BackupVerifyActivity : BaseActivity() {

    private var from = BackupActivity.FROM_CREATE
    private var id = 0L
    private lateinit var entropy: String

    private var mnemonicS = ""

    private val topList = ArrayList<String>(24)
    private val bottomList = ArrayList<String>(24)

    override fun prepare(savedInstanceState: Bundle?) {
        from = intent.getIntExtra(ExtraKey.INT, BackupActivity.FROM_CREATE)
        id = intent.getLongExtra(ExtraKey.LONG, 0L)
        entropy = intent.getStringExtra(ExtraKey.STRING) ?: ""
    }

    override fun getLayoutId() = R.layout.g_activity_backup_verify

    override fun initView() {
        title = "备份助记词"
        topList.clear()
        bottomList.clear()
        bottomList.addAll(WKey.getRandomMnemonic(WUtil.HexStringToByteArray(entropy)))
        mnemonicS = bottomList.joinToString()
        bottomList.shuffle()
        val topAdapter = MnemonicAdapter(topList)
        val bottomAdapter = MnemonicAdapter(bottomList)
        topAdapter.itemClickListener = object : OnItemClickListener<String> {
            override fun itemClicked(view: View, item: String, position: Int) {
                topList.removeAt(position)
                topAdapter.notifyDataSetChanged()

                bottomList.add(item)
                bottomList.shuffle()
                bottomAdapter.notifyDataSetChanged()

                checkCompleted()
            }
        }
        bottomAdapter.itemClickListener = object : OnItemClickListener<String> {
            override fun itemClicked(view: View, item: String, position: Int) {
                bottomList.removeAt(position)
                bottomList.shuffle()
                bottomAdapter.notifyDataSetChanged()

                topList.add(item)
                topAdapter.notifyDataSetChanged()
                checkCompleted()
            }
        }
        rvTop.init(topAdapter, 4)
        rvBottom.init(bottomAdapter, 4)

        RxView.click(btnComfirm) {
            if (mnemonicS == topList.joinToString()) {
                toast("验证成功")
                GlobalScope.launch(Dispatchers.Main) {
                    backup()
                    if (BackupActivity.FROM_CREATE == from) {
                        go(MainActivity::class.java, true)
                    } else {
                        RxBus.instance().send(RefreshWalletListEvent())
                    }
                    finish()
                }
            } else {
                toast("助记词无效")
            }
        }
    }

    private suspend fun backup() {
        withContext(Dispatchers.IO) {
            AccountManager.instance().dao().onUpdateBackup(id)
        }
    }

    private fun checkCompleted() {
        btnComfirm.isEnabled = topList.size == 24
    }

    override fun onBackPressed() {
        if (BackupActivity.FROM_CREATE == from) {
            go(MainActivity::class.java, true)
        } else {
            finish()
        }
    }

    companion object {
        fun start(context: Context, from: Int, id: Long, entropy: String) {
            val intent = Intent(context, BackupVerifyActivity::class.java)
            intent.putExtra(ExtraKey.INT, from)
            intent.putExtra(ExtraKey.LONG, id)
            intent.putExtra(ExtraKey.STRING, entropy)
            context.startActivity(intent)
        }
    }
}