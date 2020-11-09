package com.highstreet.wallet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.highstreet.wallet.R;
import com.highstreet.wallet.activities.UndelegateActivity;
import com.highstreet.wallet.base.BaseFragment;
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

public class UndelegateStep3Fragment extends BaseFragment implements View.OnClickListener {

    private TextView        mTvUndelegateAmount;
    private TextView        mFeeAmount;
    private TextView        mValidatorName, mMemo, mTime;
    private TextView        mDenomUndelegateAmount, mDenomFeeType;
    private Button          mBeforeBtn, mConfirmBtn;

    public static UndelegateStep3Fragment newInstance(Bundle bundle) {
        UndelegateStep3Fragment fragment = new UndelegateStep3Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_undelegate_step3, container, false);
        mTvUndelegateAmount     = rootView.findViewById(R.id.undelegate_amount);
        mDenomUndelegateAmount  = rootView.findViewById(R.id.undelegate_amount_title);
        mFeeAmount              = rootView.findViewById(R.id.undelegate_fees);
        mDenomFeeType           = rootView.findViewById(R.id.undelegate_fees_type);
        mValidatorName          = rootView.findViewById(R.id.undelegate_moniker);
        mMemo                   = rootView.findViewById(R.id.memo);
        mTime                   = rootView.findViewById(R.id.undelegate_time);
        mBeforeBtn              = rootView.findViewById(R.id.btn_before);
        mConfirmBtn             = rootView.findViewById(R.id.btn_confirm);

        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mDenomUndelegateAmount);
        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mDenomFeeType);

        mBeforeBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onRefreshTab() {
        BigDecimal toUnDeleagteAmount = new BigDecimal(getSActivity().mUnDelegateAmount.amount);
        BigDecimal feeAmount = new BigDecimal(getSActivity().mUnDelegateFee.amount.get(0).amount);
        if (getSActivity().mBaseChain.equals(COSMOS_MAIN) || getSActivity().mBaseChain.equals(KAVA_MAIN) || getSActivity().mBaseChain.equals(KAVA_TEST)
                || getSActivity().mBaseChain.equals(BAND_MAIN) || getSActivity().mBaseChain.equals(IOV_MAIN) || getSActivity().mBaseChain.equals(IOV_TEST) || getSActivity().mBaseChain.equals(CERTIK_TEST)) {
            mTvUndelegateAmount.setText(WDp.getDpAmount(getContext(), toUnDeleagteAmount, 6, getSActivity().mBaseChain));
            mFeeAmount.setText(WDp.getDpAmount(getContext(), feeAmount, 6, getSActivity().mBaseChain));

        } else if (getSActivity().mBaseChain.equals(IRIS_MAIN)) {
            mTvUndelegateAmount.setText(WDp.getDpAmount(getContext(), toUnDeleagteAmount, 18, getSActivity().mBaseChain));
            mFeeAmount.setText(WDp.getDpAmount(getContext(), feeAmount, 18, getSActivity().mBaseChain));

        }
        mTime.setText(WDp.getUnbondTime(getContext(), getSActivity().mBaseChain));
        mValidatorName.setText(getSActivity().mValidator.description.moniker);
        mMemo.setText(getSActivity().mUnDelegateMemo);

    }

    @Override
    public void onClick(View v) {
        if(v.equals(mBeforeBtn)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mConfirmBtn)) {
            getSActivity().onStartUndelegate();

        }
    }

    private UndelegateActivity getSActivity() {
        return (UndelegateActivity)getBaseActivity();
    }

}
