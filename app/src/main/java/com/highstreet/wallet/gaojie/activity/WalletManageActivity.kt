package com.highstreet.wallet.gaojie.activity

import android.arch.lifecycle.Observer
import android.os.Handler
import android.view.View
import com.highstreet.lib.adapter.BasePagedAdapter
import com.highstreet.lib.common.RefreshResult
import com.highstreet.lib.fingerprint.FingerprintUtils
import com.highstreet.lib.ui.BaseListActivity
import com.highstreet.lib.view.dialog.ConfirmDialog
import com.highstreet.lib.view.dialog.ConfirmDialogListener
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.crypto.CryptoHelper
import com.highstreet.wallet.dao.Account
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.adapter.WalletManageAdapter
import com.highstreet.wallet.gaojie.copy
import com.highstreet.wallet.gaojie.vm.WalletManageVM
import kotlinx.android.synthetic.main.g_activity_wallet_manage.*

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */

class WalletManageActivity : BaseListActivity<Account, WalletManageVM>() {

    /**
     * 记录一下最开始的，在onDestroy判断是否变动
     */
    private var account: Account? = null

    /**
     * 当前操作的Account
     */
    private var useAccount: Account? = null

    private var type = 0

    private lateinit var adapter: WalletManageAdapter

    override fun showToolbar() = false

    override fun getLayoutId() = R.layout.g_activity_wallet_manage

    override fun createAdapter(): BasePagedAdapter<Account> {
        adapter = WalletManageAdapter(this)
        return adapter
    }

    override fun initView() {
        super.initView()
        title = "钱包管理"
        RxView.click(ivAdd) {
            InitWalletActivity.start(this, true)
        }
    }

    override fun initData() {
        super.initData()
        account = AccountManager.instance().account
        lifecycle.addObserver(viewModel)
        viewModel.deleteLD.observe(this, Observer {
            if (true == it) {
                toast("删除成功")
                Handler().postDelayed({
                    hideLoading()
                    if (AccountManager.instance().accounts.isEmpty()) {
                        InitWalletActivity.start(this)
                    }
                }, 1000)
            } else {
                hideLoading()
                toast("删除失败")
            }
        })
    }

    override fun refreshFinished(result: RefreshResult) {
        adapter.setCurrentAccount(AccountManager.instance().account)
        super.refreshFinished(result)
    }

    override fun itemClicked(view: View, item: Account, position: Int) {
        when (view.id) {
            R.id.tvWalletAddress, R.id.ivCopy -> item.address?.copy(this)
            R.id.tvBackup -> {
                type = TYPE_BACKUP
                useAccount = item
                getFingerprint(FingerprintUtils.isAvailable(this), true)?.authenticate()
            }
            R.id.ivDelete -> {
                ConfirmDialog(this).setMsg("确认删除钱包${item.nickName}?").setListener(object : ConfirmDialogListener {
                    override fun confirm() {
                        type = TYPE_DELETE
                        useAccount = item
                        getFingerprint(FingerprintUtils.isAvailable(this@WalletManageActivity), true)?.authenticate()
                    }

                    override fun cancel() {

                    }

                }).show()
            }
            else -> {
                if (AccountManager.instance().account?.address != item.address) {
                    AccountManager.instance().account = item
                }
                adapter.setCurrentAccount(item)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        if (AccountManager.instance().account?.address != account?.address) {
            AccountManager.instance().setLastUser()
        }
        super.onDestroy()
    }

    override fun onFingerprintAuthenticateSucceed() {
        useAccount?.apply {
            if (type == TYPE_BACKUP) {
                BackupActivity.start(this@WalletManageActivity,
                        BackupActivity.FROM_WALLET_MANAGER,
                        id,
                        AccountManager.getEntropy(this)
                )
            } else if (type == TYPE_DELETE) {
                showLoading()
                viewModel.deleteAccount(this)
            }
        }
    }

    override fun usePassword(password: String): Boolean {
        val userPassword = AccountManager.instance().password
        if (!CryptoHelper.verifyData(password, userPassword?.resource
                        ?: "", AccountManager.KEY_PASSWORD)) {
            return false
        }
        onFingerprintAuthenticateSucceed()
        return true
    }

    companion object {
        private const val TYPE_BACKUP = 1
        private const val TYPE_DELETE = 2
    }
}