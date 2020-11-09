package com.highstreet.wallet.gaojie.activity

import android.content.Context
import android.content.Intent
import android.view.View
import com.highstreet.lib.extensions.visibility
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.StringUtils
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.highstreet.wallet.gaojie.copy
import com.highstreet.wallet.gaojie.model.dip.Validator
import kotlinx.android.synthetic.main.g_activity_validator_detail.*

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class ValidatorDetailActivity : BaseActivity(), View.OnClickListener {

    private var validator: Validator? = null

    override fun showToolbar() = false

    override fun getLayoutId() = R.layout.g_activity_validator_detail

    override fun initView() {
        title = "详情"
        RxView.click(tvAddress, this)
        RxView.click(ivCopy, this)
        RxView.click(btnDelegate, this)

        btnDelegate.visibility(intent.getBooleanExtra(ExtraKey.BOOLEAN,true))
    }

    override fun initData() {
        validator = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Validator?
        validator?.apply {
            tvName.text = description?.moniker
            tvAddress.text = operator_address
            tvAvatar.text = getFirstLetterName()
            tvShares.text = StringUtils.pdip2DIP(delegator_shares)
            tvSelfShares.text = StringUtils.pdip2DIP(self_delegation)
            tvRate.text = getRate()
            tvProfile.text = getProfile()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            tvAddress, ivCopy -> tvAddress.text.toString().copy(this)
            btnDelegate -> validator?.let { DelegationActivity.start(this, it) }
        }
    }

    companion object {
        fun start(context: Context, validator: Validator, enableDelegate: Boolean = true) {
            val intent = Intent(context, ValidatorDetailActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, validator)
            intent.putExtra(ExtraKey.BOOLEAN, enableDelegate)
            context.startActivity(intent)
        }
    }
}