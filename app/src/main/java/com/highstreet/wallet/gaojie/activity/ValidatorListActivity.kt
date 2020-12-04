package com.highstreet.wallet.gaojie.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.highstreet.lib.extensions.gone
import com.highstreet.lib.extensions.visible
import com.highstreet.lib.ui.BaseListActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.adapter.ValidatorAdapter
import com.highstreet.wallet.gaojie.adapter.ValidatorChooseAdapter
import com.highstreet.wallet.gaojie.constant.Constant
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.highstreet.wallet.gaojie.model.dip.Validator
import com.highstreet.wallet.gaojie.vm.ValidatorVM
import kotlinx.android.synthetic.main.g_activity_validator_list.*

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
open class ValidatorListActivity : BaseListActivity<Validator, ValidatorVM>(), View.OnClickListener {

    /**
     * 是否是来选择验证人
     */
    private var isChoose = false

    private var filterShow = false
    private var filterType = Constant.VALIDATOR_ALL
    private var sortType = Constant.SORT_SHARES_DESC
    private var rateSort = Constant.SORT_RATE_DESC
    private var sharesSort = Constant.SORT_SHARES_DESC

    override fun getLayoutId() = R.layout.g_activity_validator_list

    override fun prepare(savedInstanceState: Bundle?) {
        isChoose = intent.getBooleanExtra(ExtraKey.BOOLEAN, false)
    }

    override fun createAdapter() = if (isChoose) ValidatorChooseAdapter() else ValidatorAdapter()

    override fun initView() {
        super.initView()
        title = if (isChoose) "选择验证人" else "验证人"
        llShares.isSelected = true
        llType.setOnClickListener(this)
        llRate.setOnClickListener(this)
        llShares.setOnClickListener(this)
        llFilter.setOnClickListener(this)
        tvTypeAll.setOnClickListener(this)
        tvTypeBonded.setOnClickListener(this)
        tvTypeJailed.setOnClickListener(this)
    }

    override fun itemClicked(view: View, item: Validator, position: Int) {
        if (isChoose) {
            if (R.id.ivArrow == view.id) {
                ValidatorDetailActivity.start(this, item, false)
            } else {
                val intent = Intent()
                intent.putExtra(ExtraKey.SERIALIZABLE, item)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        } else {
            ValidatorDetailActivity.start(this, item)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            llType -> clickType()
            llRate -> clickRate()
            llShares -> clickShares()
            llFilter -> hideFilter()
            tvTypeAll -> selectedType(tvTypeAll.text.toString(), Constant.VALIDATOR_ALL)
            tvTypeBonded -> selectedType(tvTypeBonded.text.toString(), Constant.VALIDATOR_BONDED)
            tvTypeJailed -> selectedType(tvTypeJailed.text.toString(), Constant.VALIDATOR_JAILED)
        }
    }

    private fun hideFilter() {
        filterShow = false
        llFilter.gone()
    }

    private fun clickType() {
        if (filterShow) {
            hideFilter()
        } else {
            filterShow = true
            llFilter.visible()
            val type = tvType.text
            tvTypeAll.isSelected = type == tvTypeAll.text
            tvTypeBonded.isSelected = type == tvTypeBonded.text
            tvTypeJailed.isSelected = type == tvTypeJailed.text
        }
    }

    private fun clickRate() {
        llShares.isSelected = false
        if (llRate.isSelected) {
            rateSort = if (rateSort == Constant.SORT_RATE_ASC) {
                Constant.SORT_RATE_DESC
            } else {
                Constant.SORT_RATE_ASC
            }
        }
        if (rateSort == Constant.SORT_RATE_ASC) {
            ivRateArrow.setImageResource(R.drawable.selector_up)
        } else {
            ivRateArrow.setImageResource(R.drawable.selector_down)
        }
        sortType = rateSort
        llRate.isSelected = true
        viewModel.filter(filterType, sortType)
    }

    private fun clickShares() {
        llRate.isSelected = false
        if (llShares.isSelected) {
            sharesSort = if (sharesSort == Constant.SORT_SHARES_ASC) {
                Constant.SORT_SHARES_DESC
            } else {
                Constant.SORT_SHARES_ASC
            }
        }
        if (sharesSort == Constant.SORT_SHARES_ASC) {
            ivSharesArrow.setImageResource(R.drawable.selector_up)
        } else {
            ivSharesArrow.setImageResource(R.drawable.selector_down)
        }
        sortType = sharesSort
        llShares.isSelected = true
        viewModel.filter(filterType, sortType)
    }

    private fun selectedType(text: String, type: Int) {
        filterType = type
        tvType.text = text
        hideFilter()
        viewModel.filter(filterType, sortType)
    }

    companion object {
        const val REQUEST_CODE_VALIDATOR_CHOOSE = 302
        fun start(context: Activity, isChoose: Boolean = false) {
            val intent = Intent(context, ValidatorListActivity::class.java)
            intent.putExtra(ExtraKey.BOOLEAN, isChoose)
            context.startActivityForResult(intent, REQUEST_CODE_VALIDATOR_CHOOSE)
        }
    }
}