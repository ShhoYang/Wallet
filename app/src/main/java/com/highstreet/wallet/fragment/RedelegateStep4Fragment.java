package com.highstreet.wallet.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.highstreet.wallet.R;
import com.highstreet.wallet.activities.RedelegateActivity;
import com.highstreet.wallet.base.BaseFragment;
import com.highstreet.wallet.dialog.Dialog_RedelegateConfirm;
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

public class RedelegateStep4Fragment extends BaseFragment implements View.OnClickListener {

    public final static int REDELEGATE_CONFIRM_DIALOG = 6016;

    private TextView        mTvRedelegateAmount, mTvRedelegateDenom;
    private TextView        mFeeAmount, mFeeDenom;
    private TextView        mFromValidatorName, mToValidatorName, mMemo;

    private Button          mBeforeBtn, mConfirmBtn;

    public static RedelegateStep4Fragment newInstance(Bundle bundle) {
        RedelegateStep4Fragment fragment = new RedelegateStep4Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_redelegate_step4, container, false);
        mTvRedelegateAmount     = rootView.findViewById(R.id.redelegate_amount);
        mTvRedelegateDenom      = rootView.findViewById(R.id.redelegate_amount_title);
        mFeeAmount              = rootView.findViewById(R.id.redelegate_fees);
        mFeeDenom               = rootView.findViewById(R.id.redelegate_fees_type);
        mFromValidatorName      = rootView.findViewById(R.id.redelegate_from_moniker);
        mToValidatorName        = rootView.findViewById(R.id.redelegate_to_moniker);
        mMemo                   = rootView.findViewById(R.id.memo);
        mBeforeBtn              = rootView.findViewById(R.id.btn_before);
        mConfirmBtn             = rootView.findViewById(R.id.btn_confirm);

        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mTvRedelegateDenom);
        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mFeeDenom);

        mBeforeBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onRefreshTab() {
        BigDecimal toReDeleagteAmount = new BigDecimal(getSActivity().mReDelegateAmount.amount);
        BigDecimal feeAmount= new BigDecimal(getSActivity().mReDelegateFee.amount.get(0).amount);
        if (getSActivity().mBaseChain.equals(COSMOS_MAIN) || getSActivity().mBaseChain.equals(KAVA_MAIN) || getSActivity().mBaseChain.equals(KAVA_TEST) ||
                getSActivity().mBaseChain.equals(BAND_MAIN) || getSActivity().mBaseChain.equals(IOV_MAIN) || getSActivity().mBaseChain.equals(IOV_TEST) || getSActivity().mBaseChain.equals(CERTIK_TEST)) {
            mTvRedelegateAmount.setText(WDp.getDpAmount(getContext(), toReDeleagteAmount, 6, getSActivity().mBaseChain));
            mFeeAmount.setText(WDp.getDpAmount(getContext(), feeAmount, 6, getSActivity().mBaseChain));

        } else if (getSActivity().mBaseChain.equals(IRIS_MAIN)) {
            mTvRedelegateAmount.setText(WDp.getDpAmount(getContext(), toReDeleagteAmount, 18, getSActivity().mBaseChain));
            mFeeAmount.setText(WDp.getDpAmount(getContext(), feeAmount, 18, getSActivity().mBaseChain));
        }
        mFromValidatorName.setText(getSActivity().mFromValidator.description.moniker);
        mToValidatorName.setText(getSActivity().mToValidator.description.moniker);
        mMemo.setText(getSActivity().mReDelegateMemo);
    }


    private RedelegateActivity getSActivity() {
        return (RedelegateActivity)getBaseActivity();
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mBeforeBtn)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mConfirmBtn)) {
            Dialog_RedelegateConfirm dialog = Dialog_RedelegateConfirm.newInstance();
            dialog.setCancelable(true);
            dialog.setTargetFragment(this, REDELEGATE_CONFIRM_DIALOG);
            dialog.show(getFragmentManager().beginTransaction(), "dialog");

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REDELEGATE_CONFIRM_DIALOG && resultCode == Activity.RESULT_OK) {
            getSActivity().onStartRedelegate();
        }
    }
}
