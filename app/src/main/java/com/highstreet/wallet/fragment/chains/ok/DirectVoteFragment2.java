package com.highstreet.wallet.fragment.chains.ok;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.highstreet.wallet.R;
import com.highstreet.wallet.activities.chains.ok.OKVoteDirectActivity;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.base.BaseFragment;
import com.highstreet.wallet.model.type.Coin;
import com.highstreet.wallet.model.type.Fee;
import com.highstreet.wallet.utils.WDp;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.highstreet.wallet.base.BaseChain.OK_TEST;
import static com.highstreet.wallet.base.BaseConstant.FEE_OK_GAS_AMOUNT_VOTE_MUX;
import static com.highstreet.wallet.base.BaseConstant.FEE_OK_GAS_RATE_AVERAGE;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_OK_TEST;

public class DirectVoteFragment2 extends BaseFragment implements View.OnClickListener {

    private RelativeLayout mBtnGasType;
    private TextView        mTvGasType;

    private LinearLayout    mFeeLayer1;
    private TextView        mMinFeeAmount;
    private TextView        mMinFeePrice;

    private LinearLayout    mFeeLayer2;
    private TextView        mGasAmount;
    private TextView        mGasRate;
    private TextView        mGasFeeAmount;
    private TextView        mGasFeePrice;

    private LinearLayout    mFeeLayer3;
    private SeekBar         mSeekBarGas;

    private LinearLayout    mSpeedLayer;
    private ImageView       mSpeedImg;
    private TextView        mSpeedMsg;

    private Button          mBeforeBtn, mNextBtn;

    private BigDecimal      mAvailable          = BigDecimal.ZERO;
    private BigDecimal      mFeeAmount          = BigDecimal.ZERO;
    private BigDecimal      mFeePrice           = BigDecimal.ZERO;
    private BigDecimal      mEstimateGasAmount  = BigDecimal.ZERO;

    public static DirectVoteFragment2 newInstance(Bundle bundle) {
        DirectVoteFragment2 fragment = new DirectVoteFragment2();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tx_step_fee, container, false);
        mBtnGasType     = rootView.findViewById(R.id.btn_gas_type);
        mTvGasType      = rootView.findViewById(R.id.gas_type);

        mFeeLayer1      = rootView.findViewById(R.id.fee_dp_layer1);
        mMinFeeAmount   = rootView.findViewById(R.id.min_fee_amount);
        mMinFeePrice    = rootView.findViewById(R.id.min_fee_price);

        mFeeLayer2      = rootView.findViewById(R.id.fee_dp_layer2);
        mGasAmount      = rootView.findViewById(R.id.gas_amount);
        mGasRate        = rootView.findViewById(R.id.gas_rate);
        mGasFeeAmount   = rootView.findViewById(R.id.gas_fee);
        mGasFeePrice    = rootView.findViewById(R.id.gas_fee_price);

        mFeeLayer3      = rootView.findViewById(R.id.fee_dp_layer3);
        mSeekBarGas     = rootView.findViewById(R.id.gas_price_seekbar);

        mSpeedLayer     = rootView.findViewById(R.id.speed_layer);
        mSpeedImg       = rootView.findViewById(R.id.speed_img);
        mSpeedMsg       = rootView.findViewById(R.id.speed_txt);

        mBeforeBtn      = rootView.findViewById(R.id.btn_before);
        mNextBtn        = rootView.findViewById(R.id.btn_next);
        mBeforeBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        WDp.DpMainDenom(getContext(), getSActivity().mAccount.baseChain, mTvGasType);


        return rootView;
    }


    @Override
    public void onRefreshTab() {
        if (getSActivity().mBaseChain.equals(OK_TEST)) {
            mFeeLayer1.setVisibility(View.GONE);
            mFeeLayer2.setVisibility(View.VISIBLE);
            mFeeLayer3.setVisibility(View.GONE);

            mSpeedImg.setImageDrawable(getResources().getDrawable(R.drawable.fee_img));
            mSpeedMsg.setText(getString(R.string.str_fee_speed_title_ok));

            mEstimateGasAmount = (new BigDecimal(FEE_OK_GAS_AMOUNT_VOTE_MUX).multiply(new BigDecimal(""+getSActivity().mValAddesses.size()))).add(new BigDecimal(BaseConstant.FEE_OK_GAS_AMOUNT_VOTE));
            mGasAmount.setText(mEstimateGasAmount.toPlainString());
            mGasRate.setText(WDp.getDpString(FEE_OK_GAS_RATE_AVERAGE, 8));
            mFeeAmount = mEstimateGasAmount.multiply(new BigDecimal(FEE_OK_GAS_RATE_AVERAGE)).setScale(8);

            mGasFeeAmount.setText(mFeeAmount.toPlainString());
            mGasFeePrice.setText(WDp.getPriceApproximatelyDp(getSActivity(), BigDecimal.ZERO, getBaseDao().getCurrencySymbol(), getBaseDao().getCurrency()));

        }
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mBeforeBtn)) {
            getSActivity().onBeforeStep();

        } else if (v.equals(mNextBtn)) {
            if (getSActivity().mBaseChain.equals(OK_TEST)) {
                Fee fee = new Fee();
                Coin gasCoin = new Coin();
                gasCoin.denom = TOKEN_OK_TEST;
                gasCoin.amount = mFeeAmount.toPlainString();
                ArrayList<Coin> amount = new ArrayList<>();
                amount.add(gasCoin);
                fee.amount = amount;
                fee.gas = mEstimateGasAmount.toPlainString();
                getSActivity().mFee = fee;

            }
            getSActivity().onNextStep();
        }
    }

    private OKVoteDirectActivity getSActivity() {
        return (OKVoteDirectActivity)getBaseActivity();
    }
}

