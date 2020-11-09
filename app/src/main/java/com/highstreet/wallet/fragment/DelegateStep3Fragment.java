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
import com.highstreet.wallet.activities.DelegateActivity;
import com.highstreet.wallet.base.BaseFragment;
import com.highstreet.wallet.dialog.Dialog_Delegate_Warning;
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

public class DelegateStep3Fragment extends BaseFragment implements View.OnClickListener {
    public final static int SELECT_DELEGATE_CHECK = 9106;

    private TextView        mDelegateAmount;
    private TextView        mFeeAmount;
    private TextView        mValidatorName, mMemo;
    private Button          mBeforeBtn, mConfirmBtn;
    private TextView        mDenomDelegateAmount, mDenomFeeType;


    public static DelegateStep3Fragment newInstance(Bundle bundle) {
        DelegateStep3Fragment fragment = new DelegateStep3Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_delegate_step3, container, false);
        mDelegateAmount         = rootView.findViewById(R.id.delegate_atom);
        mDenomDelegateAmount    = rootView.findViewById(R.id.delegate_amount_title);
        mFeeAmount              = rootView.findViewById(R.id.delegate_fees);
        mDenomFeeType           = rootView.findViewById(R.id.delegate_fees_type);
        mValidatorName          = rootView.findViewById(R.id.to_delegate_moniker);
        mMemo                   = rootView.findViewById(R.id.memo);
        mBeforeBtn              = rootView.findViewById(R.id.btn_before);
        mConfirmBtn             = rootView.findViewById(R.id.btn_confirm);

        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mDenomDelegateAmount);
        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mDenomFeeType);

        mBeforeBtn.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onRefreshTab() {
        BigDecimal toDeleagteAmount = new BigDecimal(getSActivity().mToDelegateAmount.amount);
        BigDecimal feeAmount = new BigDecimal(getSActivity().mToDelegateFee.amount.get(0).amount);
        if (getSActivity().mBaseChain.equals(COSMOS_MAIN) || getSActivity().mBaseChain.equals(KAVA_MAIN) || getSActivity().mBaseChain.equals(KAVA_TEST) ||
                getSActivity().mBaseChain.equals(BAND_MAIN) || getSActivity().mBaseChain.equals(IOV_MAIN) || getSActivity().mBaseChain.equals(IOV_TEST) || getSActivity().mBaseChain.equals(CERTIK_TEST)) {
            mDelegateAmount.setText(WDp.getDpAmount(getContext(), toDeleagteAmount, 6, getSActivity().mBaseChain));
            mFeeAmount.setText(WDp.getDpAmount(getContext(), feeAmount, 6, getSActivity().mBaseChain));

        } else if (getSActivity().mBaseChain.equals(IRIS_MAIN)) {
            mDelegateAmount.setText(WDp.getDpAmount(getContext(), toDeleagteAmount, 18, getSActivity().mBaseChain));
            mFeeAmount.setText(WDp.getDpAmount(getContext(), feeAmount, 18, getSActivity().mBaseChain));

        }
        mValidatorName.setText(getSActivity().mValidator.description.moniker);
        mMemo.setText(getSActivity().mToDelegateMemo);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mBeforeBtn)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mConfirmBtn)) {
            Bundle bundle = new Bundle();
            if (getSActivity().mBaseChain.equals(IOV_MAIN) || getSActivity().mBaseChain.equals(IOV_TEST)) {
                bundle.putInt("day", 3);
            } else if (getSActivity().mBaseChain.equals(CERTIK_TEST)) {
                bundle.putInt("day", 14);
            } else {
                bundle.putInt("day", 21);
            }
            Dialog_Delegate_Warning dialog = Dialog_Delegate_Warning.newInstance(bundle);
            dialog.setTargetFragment(DelegateStep3Fragment.this, SELECT_DELEGATE_CHECK);
            getFragmentManager().beginTransaction().add(dialog, "dialog").commitNowAllowingStateLoss();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_DELEGATE_CHECK && resultCode == Activity.RESULT_OK) {
            getSActivity().onStartDelegate();
        }
    }

    private DelegateActivity getSActivity() {
        return (DelegateActivity)getBaseActivity();
    }


}
