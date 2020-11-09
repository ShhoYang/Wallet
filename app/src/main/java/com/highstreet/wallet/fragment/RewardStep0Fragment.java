package com.highstreet.wallet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highstreet.wallet.R;
import com.highstreet.wallet.activities.ClaimRewardActivity;
import com.highstreet.wallet.base.BaseFragment;
import com.highstreet.wallet.dao.Reward;
import com.highstreet.wallet.model.type.Validator;
import com.highstreet.wallet.utils.WDp;

import java.math.BigDecimal;

import static com.highstreet.wallet.base.BaseChain.BAND_MAIN;
import static com.highstreet.wallet.base.BaseChain.CERTIK_TEST;
import static com.highstreet.wallet.base.BaseChain.COSMOS_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_TEST;
import static com.highstreet.wallet.base.BaseChain.IRIS_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_TEST;

public class RewardStep0Fragment extends BaseFragment implements View.OnClickListener {

    private CardView        mCardReward;
    private TextView        mTvRewardAmount, mTvDenomTitle;
    private TextView        mTvFromValidators;

    private LinearLayout    mReceiveLayer;
    private TextView        mTvReceiveAddress;
    private RelativeLayout  mProgressBar;

    private Button          mCancelBtn, mNextBtn;


    public static RewardStep0Fragment newInstance(Bundle bundle) {
        RewardStep0Fragment fragment = new RewardStep0Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reward_step0, container, false);
        mCardReward             = rootView.findViewById(R.id.reward_card);
        mTvRewardAmount         = rootView.findViewById(R.id.reward_amount);
        mTvDenomTitle           = rootView.findViewById(R.id.reward_denom_title);
        mTvFromValidators       = rootView.findViewById(R.id.reward_moniker);
        mReceiveLayer           = rootView.findViewById(R.id.reward_receive_address_layer);
        mTvReceiveAddress       = rootView.findViewById(R.id.reward_receive_address);
        mProgressBar            = rootView.findViewById(R.id.reward_progress);
        mCancelBtn              = rootView.findViewById(R.id.btn_cancel);
        mNextBtn                = rootView.findViewById(R.id.btn_next);

        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mTvDenomTitle);

        mCancelBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mNextBtn.setClickable(false);
        return rootView;
    }

    @Override
    public void onRefreshTab() {
        if (getSActivity().mBaseChain.equals(COSMOS_MAIN) || getSActivity().mBaseChain.equals(KAVA_MAIN) || getSActivity().mBaseChain.equals(KAVA_TEST)
                || getSActivity().mBaseChain.equals(BAND_MAIN) || getSActivity().mBaseChain.equals(IOV_MAIN) || getSActivity().mBaseChain.equals(IOV_TEST) || getSActivity().mBaseChain.equals(CERTIK_TEST)) {
            BigDecimal rewardSum = BigDecimal.ZERO;
            for (Reward reward:getSActivity().mRewards) {
                rewardSum = rewardSum.add(new BigDecimal(reward.amount.get(0).amount).setScale(0, BigDecimal.ROUND_DOWN));
            }
            mTvRewardAmount.setText(WDp.getDpAmount(getContext(), rewardSum, 6, getSActivity().mBaseChain));
        } else if (getSActivity().mBaseChain.equals(IRIS_MAIN)) {
            mTvRewardAmount.setText(WDp.getDpAmount(getContext(), getSActivity().getIrisRewardSum(), 18, getSActivity().mBaseChain));
        }

        String monikers = "";
        for (Validator validator:getSActivity().mValidators) {
            if(TextUtils.isEmpty(monikers)) {
                monikers = validator.description.moniker;
            } else {
                monikers = monikers + ",    " + validator.description.moniker;
            }
        }
        mTvFromValidators.setText(monikers);

        mTvReceiveAddress.setText(getSActivity().mWithdrawAddress);
        if(getSActivity().mWithdrawAddress.equals(getSActivity().mAccount.address)) {
            mReceiveLayer.setVisibility(View.GONE);
        } else {
            mReceiveLayer.setVisibility(View.VISIBLE);
        }
        mProgressBar.setVisibility(View.GONE);
        mNextBtn.setClickable(true);
    }


    @Override
    public void onClick(View v) {
        if (v.equals(mCancelBtn)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mNextBtn)) {
            getSActivity().onNextStep();
        }
    }

    private ClaimRewardActivity getSActivity() {
        return (ClaimRewardActivity)getBaseActivity();
    }
}
