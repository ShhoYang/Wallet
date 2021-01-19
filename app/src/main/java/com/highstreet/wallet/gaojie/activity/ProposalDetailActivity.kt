package com.highstreet.wallet.gaojie.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.view.View
import com.highstreet.lib.extensions.visibility
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.StringUtils
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.highstreet.wallet.gaojie.constant.ProposalOpinion
import com.highstreet.wallet.gaojie.model.dip.FinalTallyResult
import com.highstreet.wallet.gaojie.model.dip.Proposal
import com.highstreet.wallet.gaojie.vm.ProposalDetailVM
import kotlinx.android.synthetic.main.g_activity_proposal_detail.*
import java.math.BigDecimal
import java.text.NumberFormat

/**
 * @author Yang Shihao
 * @Date 2020/10/28
 */
class ProposalDetailActivity : BaseActivity(), View.OnClickListener {

    private var proposal: Proposal? = null

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ProposalDetailVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_proposal_detail

    override fun initView() {
        proposal = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as Proposal?
        setData(proposal)
        RxView.click(llYes, this)
        RxView.click(llNo, this)
        RxView.click(llNoWithVeto, this)
        RxView.click(llAbstain, this)
    }

    override fun initData() {

        viewModel.proposalLD.observe(this, Observer {
            setData(it)
        })

        viewModel.rateLD.observe(this, Observer {
            calculateRate(it)
        })

        viewModel.opinionLD.observe(this, Observer {
            tvOpinion.text = it
        })

        viewModel.voteLD.observe(this, Observer {
            hideLoading()
            toast(it?.second)
        })

        proposal?.apply {
            viewModel.votingRate(id)
            viewModel.proposalOpinion(id)
        }
    }

    private fun setData(proposal: Proposal?) {
        proposal?.apply {
            title = "#$id"
            statusPoint.setBackgroundResource(if (isPassed()) R.drawable.shape_circle_green else R.drawable.shape_circle_red)
            tvStatus.text = getStatus()
            tvTitle.text = content?.value?.title
            tvDesc.text = content?.value?.description
            tvProposer.text = proposer
            tvType.text = content?.type
            tvSubmitTime.text = StringUtils.utc2String(submit_time)
            tvVotingStartTime.text = StringUtils.utc2String(voting_start_time)
            tvVotingEndTime.text = StringUtils.utc2String(voting_end_time)
            llVote.visibility(isVotingPeriod())
        }
    }

    private fun calculateRate(finalTallyResult: FinalTallyResult?) {
        finalTallyResult?.let {
            val yes = BigDecimal(it.yes ?: "0")
            val noWithVeto = BigDecimal(it.no_with_veto ?: "0")
            val no = BigDecimal(it.no ?: "0")
            val abstain = BigDecimal(it.abstain ?: "0")
            var total = yes.add(noWithVeto).add(no).add(abstain)
            if (total.compareTo(BigDecimal(0)) == 0) {
                tvYes.text = "0%"
                progressYes.progress = 0

                tvNoWithVeto.text = "0%"
                progressNoWithVeto.progress = 0

                tvNo.text = "0%"
                progressNo.progress = 0

                tvAbstain.text = "0%"
                progressAbstain.progress = 0
            } else {
                val maxProcess = BigDecimal(100)

                val yesRate = yes.divide(total)
                val noWithVetoRate = noWithVeto.divide(total)
                val noRate = no.divide(total)
                val abstainRate = abstain.divide(total)
//                val df = DecimalFormat("#.####%")
                val df = NumberFormat.getPercentInstance()
                df.maximumFractionDigits = 4
                tvYes.text = df.format(yesRate)
                progressYes.progress = yesRate.multiply(maxProcess).toInt()

                tvNoWithVeto.text = df.format(noWithVetoRate)
                progressNoWithVeto.progress = noWithVetoRate.multiply(maxProcess).toInt()

                tvNo.text = df.format(noRate)
                progressNo.progress = noRate.multiply(maxProcess).toInt()

                tvAbstain.text = df.format(abstainRate)
                progressAbstain.progress = abstainRate.multiply(maxProcess).toInt()
            }

//            progressYes.progress = 5000
//            progressNoWithVeto.progress = 2500
//            progressNo.progress = 1500
//            progressAbstain.progress = 1000
        }
    }


    override fun onClick(v: View?) {
        when (v) {
            llYes -> vote(ProposalOpinion.YES)
            llNo -> vote(ProposalOpinion.NO)
            llNoWithVeto -> vote(ProposalOpinion.NO_WITH_VETO)
            llAbstain -> vote(ProposalOpinion.ABSTAIN)
        }
    }

    private fun vote(opinion: String) {
        proposal?.apply {
            showLoading()
            viewModel.vote(id, opinion)
        }
    }

    companion object {
        fun start(context: Context, proposal: Proposal) {
            val intent = Intent(context, ProposalDetailActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, proposal)
            context.startActivity(intent)
        }
    }
}