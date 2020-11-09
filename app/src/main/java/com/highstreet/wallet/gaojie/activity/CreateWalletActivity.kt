package com.highstreet.wallet.gaojie.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.highstreet.lib.common.AppManager
import com.highstreet.lib.extensions.string
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.constant.Colors
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.highstreet.wallet.gaojie.isName
import com.highstreet.wallet.gaojie.model.WalletParams
import com.highstreet.wallet.gaojie.vm.CreateWalletVM
import com.highstreet.wallet.utils.WKey
import kotlinx.android.synthetic.main.g_activity_create_wallet.*

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
class CreateWalletActivity : BaseActivity() {

    private val walletParams = WalletParams.instance()
    private var chain = ""

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CreateWalletVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_create_wallet

    override fun prepare(savedInstanceState: Bundle?) {
        chain = intent.getStringExtra(ExtraKey.STRING) ?: ""
    }

    override fun initView() {
        title = "创建钱包"

        etName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus -> nameLine.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur) }

        RxView.textChanges(etName) {
            btnCreate.isEnabled = etName.string().isNotEmpty()
        }

        RxView.click(btnCreate) {
            createWallet()
        }
    }

    override fun initData() {

        walletParams.entropy = WKey.getEntropy()
        etAddress.setText(walletParams.address)
        viewModel.resultLD.observe(this, Observer {
            hideLoading()
            if (true == it) {
                AppManager.instance().finishActivity(InitWalletActivity::class.java)
                val account = AccountManager.instance().account!!
                BackupActivity.start(this, BackupActivity.FROM_CREATE, account.id, AccountManager.getEntropy(account))
                finish()
            } else {
                toast("创建钱包失败")
            }
        })
    }

    private fun createWallet() {
        if (null == AccountManager.instance().password) {
            toast("请先设置密码")
            CreatePasswordActivity.start(this)
            return
        }

        val name = etName.string()

        if (!name.isName()) {
            toast("输入的钱包名字无效")
            return
        }

        showLoading()
        viewModel.createWallet(name, walletParams)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CreatePasswordActivity.REQUEST_CODE_CREATE_PASSWORD && resultCode == Activity.RESULT_OK) {
            createWallet()
        }
    }

    companion object {
        fun start(context: Context, chain: String, isAdd: Boolean) {
            val intent = Intent(context, CreateWalletActivity::class.java)
            intent.putExtra(ExtraKey.STRING, chain)
            intent.putExtra(ExtraKey.BOOLEAN, isAdd)
            context.startActivity(intent)
        }
    }
}