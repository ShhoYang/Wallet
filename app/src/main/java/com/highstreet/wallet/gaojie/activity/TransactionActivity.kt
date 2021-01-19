package com.highstreet.wallet.gaojie.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.highstreet.lib.extensions.string
import com.highstreet.lib.fingerprint.FingerprintUtils
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.crypto.CryptoHelper
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.StringUtils
import com.highstreet.wallet.gaojie.constant.Colors
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.highstreet.wallet.gaojie.isAddress
import com.highstreet.wallet.gaojie.isAmount
import com.highstreet.wallet.gaojie.vm.TransactionVM
import kotlinx.android.synthetic.main.g_activity_transaction.*

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */

class TransactionActivity : BaseActivity(), View.OnClickListener, View.OnFocusChangeListener {


    private var longAmount = 0L
    private var amount = ""

    override fun showToolbar() = false

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TransactionVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_transaction

    override fun initView() {
        title = "转账"
        etToAddress.onFocusChangeListener = this
        etAmount.onFocusChangeListener = this
        etRemarks.onFocusChangeListener = this

        RxView.textChanges(etAmount) {
            btnConfirm.isEnabled = etAmount.string().isNotEmpty()
        }
        RxView.click(ivScan, this)
        RxView.click(btnConfirm, this)
        RxView.click(tvAll, this)
    }

    private fun transact() {

        val address = etToAddress.string()
        if (!address.isAddress()) {
            toast("地址无效")
            return
        }
        val s = etAmount.string()
        if (TextUtils.isEmpty(s) || !s.isAmount()) {
            toast("金额不合法")
            return
        }

        getFingerprint(FingerprintUtils.isAvailable(this) && AccountManager.instance().fingerprint, true)?.authenticate()
    }

    private fun updateLineStyle(view: View, hasFocus: Boolean) {
        view.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur)
    }

    override fun initData() {
        viewModel.amountLD.observe(this, Observer {
            it?.apply {
                longAmount = getLongAmount()
                val dip = StringUtils.pdip2DIP(getAmount())
                amount = dip.substring(0, dip.length - 3)
                tvBalance.text = "可用余额${amount}DIP"
            }
        })
        viewModel.resultLD.observe(this, Observer {
            hideLoading()
            toast(it?.second)
            if (true == it?.first) {
                finish()
            }
        })
        viewModel.getAccountInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanActivity.REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            val address = data?.getStringExtra(ExtraKey.STRING)

            if (null !== address) {
                if (address.isAddress()) {
                    etToAddress.setText(address)
                } else {
                    toast("无效的地址")
                }
            }
        }
    }

    override fun onFingerprintAuthenticateSucceed() {
        showLoading()
        viewModel.transact(etToAddress.string(), etAmount.string(), longAmount, etAmount.string() == amount, etRemarks.string())
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

    override fun onClick(v: View?) {
        when (v) {
            ivScan -> ScanActivity.start(this)
            btnConfirm -> transact()
            tvAll -> etAmount.setText(amount)

        }
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        when (v) {
            etToAddress -> updateLineStyle(toAddressLine, hasFocus)
            etAmount -> updateLineStyle(amountLine, hasFocus)
            etRemarks -> updateLineStyle(remarksLine, hasFocus)
        }
    }
}