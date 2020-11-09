package com.highstreet.wallet.gaojie.activity

import android.app.Activity
import android.content.Intent
import android.view.View
import com.highstreet.lib.ui.BaseListActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.adapter.ValidatorChooseAdapter
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.highstreet.wallet.gaojie.fragment.ValidatorFragment
import com.highstreet.wallet.gaojie.model.dip.Validator
import com.highstreet.wallet.gaojie.vm.ValidatorVM

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class ValidatorChooseActivity : BaseListActivity<Validator, ValidatorVM>() {

    override fun createAdapter() = ValidatorChooseAdapter()

    override fun initView() {
        title = "选择验证人"
        super.initView()
    }

    override fun initData() {
        viewModel.type = ValidatorFragment.TYPE_ALL
        super.initData()
    }

    override fun itemClicked(view: View, item: Validator, position: Int) {
        if (R.id.ivArrow == view.id) {
            ValidatorDetailActivity.start(this, item, false)
        } else {
            val intent = Intent()
            intent.putExtra(ExtraKey.SERIALIZABLE, item)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        const val REQUEST_CODE_VALIDATOR_CHOOSE = 302
        fun start(context: Activity) {
            context.startActivityForResult(Intent(context, ValidatorChooseActivity::class.java), REQUEST_CODE_VALIDATOR_CHOOSE)
        }
    }
}