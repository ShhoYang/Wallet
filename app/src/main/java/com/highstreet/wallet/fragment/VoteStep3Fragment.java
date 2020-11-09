package com.highstreet.wallet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.highstreet.wallet.R;
import com.highstreet.wallet.activities.VoteActivity;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseFragment;
import com.highstreet.wallet.utils.WDp;

import java.math.BigDecimal;

public class VoteStep3Fragment extends BaseFragment implements View.OnClickListener {

    private TextView mProposalTitle;
    private TextView mProposer;
    private TextView mOpinion;
    private TextView mFeeAmount, mDenomFeeType;
    private TextView mMemo;
    private Button mBeforeBtn, mConfirmBtn;


    public static VoteStep3Fragment newInstance(Bundle bundle) {
        VoteStep3Fragment fragment = new VoteStep3Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vote_step3, container, false);
        mProposalTitle = rootView.findViewById(R.id.vote_title);
        mProposer = rootView.findViewById(R.id.vote_proposer);
        mOpinion = rootView.findViewById(R.id.vote_to);
        mFeeAmount = rootView.findViewById(R.id.vote_fees);
        mDenomFeeType = rootView.findViewById(R.id.vote_fees_type);
        mMemo = rootView.findViewById(R.id.memo);
        mBeforeBtn = rootView.findViewById(R.id.btn_before);
        mConfirmBtn = rootView.findViewById(R.id.btn_confirm);

        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mDenomFeeType);
        mProposalTitle.setText(getSActivity().mProposeTitle);
        mProposer.setText(getSActivity().mProposer);
        mBeforeBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onRefreshTab() {
        BigDecimal feeAmount = new BigDecimal(getSActivity().mFee.amount.get(0).amount);
        if (getSActivity().mBaseChain.equals(BaseChain.COSMOS_MAIN) || getSActivity().mBaseChain.equals(BaseChain.KAVA_MAIN)) {
            mFeeAmount.setText(WDp.getDpAmount(getContext(), feeAmount, 6, getSActivity().mBaseChain));

        } else if (getSActivity().mBaseChain.equals(BaseChain.IRIS_MAIN)) {
            mFeeAmount.setText(WDp.getDpAmount(getContext(), feeAmount, 18, getSActivity().mBaseChain));

        }
        mOpinion.setText(getSActivity().mOpinion);
        mMemo.setText(getSActivity().mMemo);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBeforeBtn)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mConfirmBtn)) {
            getSActivity().onStartVote();

        }

    }

    private VoteActivity getSActivity() {
        return (VoteActivity)getBaseActivity();
    }
}
