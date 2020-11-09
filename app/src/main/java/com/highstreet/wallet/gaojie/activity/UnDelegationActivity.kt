package com.highstreet.wallet.gaojie.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.highstreet.lib.common.AppManager
import com.highstreet.lib.extensions.string
import com.highstreet.lib.fingerprint.FingerprintUtils
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.crypto.CryptoHelper
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.AmountUtils
import com.highstreet.wallet.gaojie.StringUtils
import com.highstreet.wallet.gaojie.constant.Colors
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.highstreet.wallet.gaojie.isAmount
import com.highstreet.wallet.gaojie.model.dip.DelegationInfo
import com.highstreet.wallet.gaojie.vm.UnDelegationVM
import kotlinx.android.synthetic.main.g_activity_un_delegation.*

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */
class UnDelegationActivity : BaseActivity(), View.OnFocusChangeListener {

    private var amount = "0"

    private var delegationInfo: DelegationInfo? = null

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(UnDelegationVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_un_delegation

    override fun initView() {
        title = "解委托"
        etAmount.onFocusChangeListener = this
        etRemarks.onFocusChangeListener = this

        RxView.textChanges(etAmount) {
            btnConfirm.isEnabled = etAmount.string().isNotEmpty()
        }

        RxView.click(tvAll) {
            etAmount.setText(amount.toString())
            etAmount.setSelection(etAmount.string().length)
        }

        RxView.click(btnConfirm) {
            unDelegate()
        }
    }

    override fun initData() {
        delegationInfo = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as DelegationInfo?
        delegationInfo?.apply {
            etAddress.setText(validator_address)
            amount = shares ?: "0"
            tvMaxAmount.text = "最多可解委托${StringUtils.pdip2DIP(amount)}"
        }
        viewModel.unDelegateLD.observe(this, Observer {
            hideLoading()
            toast(it?.second)
            if (true == it?.first) {
                AppManager.instance().finishActivity(DelegationDetailActivity::class.java)
                finish()
            }
        })
    }

    private fun unDelegate() {
        if (delegationInfo == null) {
            return
        }
        val s = etAmount.string()
        if (TextUtils.isEmpty(s) || !s.isAmount()) {
            toast("解委托数量不合法")
            return
        }

        if (!AmountUtils.isEnough(amount, s)) {
            toast("超出最大可解委托数量")
            return
        }

        getFingerprint(FingerprintUtils.isAvailable(this) && AccountManager.instance().fingerprint, true)?.authenticate()
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        when (v) {
            etAmount -> updateLineStyle(amountLine, hasFocus)
            etRemarks -> updateLineStyle(remarksLine, hasFocus)
        }
    }

    private fun updateLineStyle(view: View, hasFocus: Boolean) {
        view.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur)
    }

    override fun onFingerprintAuthenticateSucceed() {
        showLoading()
        viewModel.unDelegate(etAmount.string(), delegationInfo!!)
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
        fun start(context: Context, delegationInfo: DelegationInfo) {
            val intent = Intent(context, UnDelegationActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, delegationInfo)
            context.startActivity(intent)
        }
    }
}