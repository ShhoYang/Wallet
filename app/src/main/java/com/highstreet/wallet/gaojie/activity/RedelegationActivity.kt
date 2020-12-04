package com.highstreet.wallet.gaojie.activity

import android.app.Activity
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
import com.highstreet.wallet.gaojie.model.dip.Validator
import com.highstreet.wallet.gaojie.vm.RedelegationVM
import kotlinx.android.synthetic.main.g_activity_redelegation.*

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 *
 * 转委托
 */
class RedelegationActivity : BaseActivity(), View.OnClickListener, View.OnFocusChangeListener {

    private var amount = "0"

    private var delegationInfo: DelegationInfo? = null
    private var validator: Validator? = null

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RedelegationVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_redelegation

    override fun initView() {
        title = "转委托"
        etAmount.onFocusChangeListener = this
        etRemarks.onFocusChangeListener = this

        RxView.textChanges(etAmount) {
            btnConfirm.isEnabled = etAmount.string().isNotEmpty()
        }

        RxView.click(llAddress, this)
        RxView.click(tvAll, this)
        RxView.click(btnConfirm, this)
    }

    override fun initData() {
        delegationInfo = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as DelegationInfo?
        delegationInfo?.apply {
            amount = shares ?: "0"
            tvMaxAmount.text = "最多可转委托${StringUtils.pdip2DIP(amount)}"
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

    private fun redelegate() {
        if (delegationInfo == null) {
            return
        }

        if (etAddress.string().isEmpty()) {
            toast("请选择验证人")
            return
        }

        val s = etAmount.string()
        if (TextUtils.isEmpty(s) || !s.isAmount()) {
            toast("转委托数量不合法")
            return
        }
        if (!AmountUtils.isEnough(amount, s)) {
            toast("超出最大可转委托数量")
            return
        }

        getFingerprint(FingerprintUtils.isAvailable(this) && AccountManager.instance().fingerprint, true)?.authenticate()
    }

    override fun onClick(v: View?) {
        when (v) {
            llAddress -> ValidatorListActivity.start(this, true)
            tvAll -> {
                etAmount.setText(amount)
                etAmount.setSelection(etAmount.string().length)
            }
            btnConfirm -> redelegate()
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ValidatorListActivity.REQUEST_CODE_VALIDATOR_CHOOSE
                && resultCode == Activity.RESULT_OK
                && data != null) {
            val validator = data.getSerializableExtra(ExtraKey.SERIALIZABLE) as Validator?
            if (delegationInfo != null && delegationInfo!!.validator_address == validator?.operator_address) {
                toast("不同选择同一个验证人进行转委托")
            } else {
                this.validator = validator
                this.validator?.apply {
                    etAddress.setText(operator_address)
                }
            }
        }
    }

    override fun onFingerprintAuthenticateSucceed() {
        showLoading()
        viewModel.redelegate(etAmount.string(), delegationInfo!!, etAddress.string())
    }

    override fun usePassword(password: String): Boolean {
        val userPassword = AccountManager.instance().password
        if (!CryptoHelper.verifyData(password, userPassword?.resource ?: "",
                        AccountManager.KEY_PASSWORD)) {
            return false
        }
        onFingerprintAuthenticateSucceed()
        return true
    }

    companion object {
        fun start(context: Context, delegationInfo: DelegationInfo) {
            val intent = Intent(context, RedelegationActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, delegationInfo)
            context.startActivity(intent)
        }
    }
}