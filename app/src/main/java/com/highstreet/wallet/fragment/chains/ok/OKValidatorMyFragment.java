package com.highstreet.wallet.fragment.chains.ok;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.highstreet.wallet.R;
import com.highstreet.wallet.activities.chains.ok.OKValidatorListActivity;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseFragment;
import com.highstreet.wallet.model.type.Validator;
import com.highstreet.wallet.network.res.ResOkDeposit;
import com.highstreet.wallet.utils.WDp;
import com.highstreet.wallet.utils.WUtil;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class OKValidatorMyFragment extends BaseFragment implements View.OnClickListener {

    private SwipeRefreshLayout          mSwipeRefreshLayout;
    private RecyclerView                mRecyclerView;
    private OKMyValidatorAdapter        mOKMyValidatorAdapter;
    private TextView                    mValidatorSize;
    private Button                      mVote;

    private ArrayList<Validator>        mMyValidators = new ArrayList<>();
    private ResOkDeposit                mOkDeposit;

    public static OKValidatorMyFragment newInstance(Bundle bundle) {
        OKValidatorMyFragment fragment = new OKValidatorMyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_okvalidator_my, container, false);
        mSwipeRefreshLayout     = rootView.findViewById(R.id.layer_refresher);
        mRecyclerView           = rootView.findViewById(R.id.recycler);
        mValidatorSize          = rootView.findViewById(R.id.validator_cnt);
        mVote                   = rootView.findViewById(R.id.btn_vote);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSActivity().onFetchAllData();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(50);
        mRecyclerView.setDrawingCacheEnabled(true);
        mOKMyValidatorAdapter = new OKMyValidatorAdapter();
        mRecyclerView.setAdapter(mOKMyValidatorAdapter);

        mVote.setOnClickListener(this);

        onRefreshTab();
        return rootView;
    }

    @Override
    public void onRefreshTab() {
        if(!isAdded()) return;
        mMyValidators.clear();
        ArrayList<Validator> allValidator = new ArrayList<>();
        allValidator.addAll(getBaseDao().mTopValidators);
        allValidator.addAll(getBaseDao().mOtherValidators);
        mOkDeposit = getBaseDao().mOkDeposit;

        for (Validator val:allValidator) {
            if (checkIsMyValidator(val.operator_address)) {
                mMyValidators.add(val);
            }
        }
        mValidatorSize.setText(""+mMyValidators.size());
        onSortValidator();

        mOKMyValidatorAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onBusyFetch() {
        if(!isAdded()) return;
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private OKValidatorListActivity getSActivity() {
        return (OKValidatorListActivity)getBaseActivity();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mVote)) {
            getSActivity().onStartDirectVote();
        }
    }

    public class OKMyValidatorAdapter extends RecyclerView.Adapter<OKMyValidatorAdapter.OKMyValidatorHolder> {

        @NonNull
        @Override
        public OKMyValidatorHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new OKMyValidatorHolder(getLayoutInflater().inflate(R.layout.item_ok_validator, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull OKMyValidatorHolder holder, int position) {
            final Validator validator  = mMyValidators.get(position);
            if (getSActivity().mBaseChain.equals(BaseChain.OK_TEST)) {
                holder.itemRoot.setCardBackgroundColor(getResources().getColor(R.color.colorTransBg9));
                holder.itemTvMoniker.setText(validator.description.moniker);
                holder.itemTvVotingPower.setText(WDp.getDpAmount2(getContext(), new BigDecimal(validator.delegator_shares), 0, 8));
                holder.itemTvCommission.setText(WDp.getCommissionRate(validator.commission.commission_rates.rate));

                String imgUrl = validator.description.identity;
                if (!TextUtils.isEmpty(imgUrl) && imgUrl.startsWith("logo|||")) {
                    imgUrl = imgUrl.replace("logo|||" , "");
                    try {
                        Picasso.get().load(imgUrl)
                                .fit().placeholder(R.drawable.validator_none_img).error(R.drawable.validator_none_img)
                                .into(holder.itemAvatar);
                    } catch (Exception e){}
                } else {
                    holder.itemAvatar.setImageDrawable(getResources().getDrawable(R.drawable.validator_none_img));
                }

                if(validator.jailed) {
                    holder.itemAvatar.setBorderColor(getResources().getColor(R.color.colorRed));
                    holder.itemRevoked.setVisibility(View.VISIBLE);
                } else {
                    holder.itemAvatar.setBorderColor(getResources().getColor(R.color.colorGray3));
                    holder.itemRevoked.setVisibility(View.GONE);
                }

            }
        }

        @Override
        public int getItemCount() {
            return mMyValidators.size();
        }

        public class OKMyValidatorHolder extends RecyclerView.ViewHolder {
            CardView            itemRoot;
            CircleImageView     itemAvatar;
            ImageView           itemRevoked;
            ImageView           itemFree;
            TextView            itemTvMoniker;
            TextView            itemTvVotingPower;
            TextView            itemTvCommission;

            public OKMyValidatorHolder(@NonNull View itemView) {
                super(itemView);
                itemRoot            = itemView.findViewById(R.id.card_validator);
                itemAvatar          = itemView.findViewById(R.id.avatar_validator);
                itemRevoked         = itemView.findViewById(R.id.avatar_validator_revoke);
                itemFree            = itemView.findViewById(R.id.avatar_validator_free);
                itemTvMoniker       = itemView.findViewById(R.id.moniker_validator);
                itemTvVotingPower = itemView.findViewById(R.id.delegate_power_validator);
                itemTvCommission    = itemView.findViewById(R.id.delegate_commission_validator);
            }
        }
    }

    private boolean checkIsMyValidator(String valAddress){
        boolean myVal = false;
        if (mOkDeposit == null || mOkDeposit.validator_address == null) {
            return myVal;
        }
        for (String val:mOkDeposit.validator_address) {
            if (val.equals(valAddress)){
                return true;
            }
        }
        return myVal;
    }

    public void onSortValidator() {
        WUtil.onSortByOKValidatorPower(mMyValidators);
    }

}
