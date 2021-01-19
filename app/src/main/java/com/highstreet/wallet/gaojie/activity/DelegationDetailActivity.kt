package com.highstreet.wallet.gaojie.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.highstreet.lib.extensions.visibility
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.StringUtils
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.highstreet.wallet.gaojie.model.dip.DelegationInfo
import com.highstreet.wallet.gaojie.model.dip.Validator
import com.highstreet.wallet.gaojie.vm.DelegationDetailVM
import kotlinx.android.synthetic.main.g_activity_delegation_detail.*

/**
 * @author Yang Shihao
 * @Date 2020/10/27
 */
class DelegationDetailActivity : BaseActivity(), View.OnClickListener {

    private var delegationInfo: DelegationInfo? = null
    private var validator: Validator? = null
    private var reward: String = "0"

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(DelegationDetailVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_delegation_detail

    override fun initView() {
        title = "委托详情"

        RxView.click(tvDetail, this)
        RxView.click(ivDetail, this)
        RxView.click(llRedelegate, this)
        RxView.click(llUnDelegate, this)
        RxView.click(llReward, this)
        RxView.click(llDelegate, this)
    }

    override fun initData() {
        val isUnDelegate = intent.getBooleanExtra(ExtraKey.BOOLEAN, false)
        llRedelegate.visibility(!isUnDelegate)
        llUnDelegate.visibility(!isUnDelegate)
        llReward.visibility(!isUnDelegate)
        llDelegate.visibility(!isUnDelegate)
        tvUnDelegateAmount.visibility(isUnDelegate)
        viewModel.validatorLD.observe(this, Observer {
            validator = it
            validator?.apply {
                tvAvatar.text = getFirstLetterName()
                tvName.text = description?.moniker
                tvAddress.text = operator_address
                tvRate.text = getRate()
            }
        })
        viewModel.rewardLD.observe(this, Observer {
            reward = it ?: "0"
            tvReward.text = reward
        })

        delegationInfo = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as DelegationInfo?
        delegationInfo?.apply {
            viewModel.getValidator(validator_address)
            viewModel.getReward(validator_address)
            tvAmount.text = StringUtils.pdip2DIP(shares, false)
            tvReward.text = reward
            if (isUnDelegate) {
                tvUnDelegateAmount.text = "${StringUtils.formatDecimal(shares)}解委托中\n（剩余${StringUtils.timeGap(completionTime)}）"
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            tvDetail, ivDetail -> {
                if (null != validator) {
                    ValidatorDetailActivity.start(this, validator!!)
                }
            }
            llRedelegate -> {
                if (null != delegationInfo) {
                    RedelegationActivity.start(this, delegationInfo!!)
                }
            }
            llUnDelegate -> {
                if (null != delegationInfo) {
                    UnDelegationActivity.start(this, delegationInfo!!)
                }
            }
            llReward -> {
                if (TextUtils.isEmpty(reward) || "0" == reward || reward.toDouble() == 0.0) {
                    toast("奖励为0")

                } else {
                    val validatorAddress = delegationInfo?.validator_address
                    val delegatorAddress = delegationInfo?.delegator_address
                    if (!TextUtils.isEmpty(validatorAddress) && !TextUtils.isEmpty(delegatorAddress)) {
                        ReceiveRewardActivity.start(this, validatorAddress!!, delegatorAddress!!, reward + "DIP")
                    }
                }
            }
            llDelegate -> {
                if (null != validator) {
                    DelegationActivity.start(this, validator!!)
                }
            }
        }
    }

    companion object {
        fun start(context: Context, delegationInfo: DelegationInfo, isUnDelegate: Boolean = false) {
            val intent = Intent(context, DelegationDetailActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, delegationInfo)
            intent.putExtra(ExtraKey.BOOLEAN, isUnDelegate)
            context.startActivity(intent)
        }
    }
}